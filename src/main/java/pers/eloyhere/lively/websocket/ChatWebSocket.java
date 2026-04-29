package pers.eloyhere.lively.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Component("chatWebSocket")
public class ChatWebSocket extends TextWebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private static final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);

    private String apiKey = "sk-bd31f366ccee4aa4bb4e317bd3e1f3ff";

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Authentication authentication = (Authentication) session.getAttributes().get("authentication");
        if (authentication == null || !authentication.isAuthenticated()) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }
        scheduleHeartbeat(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Map<String, Object> json = mapper.readValue(payload, Map.class);
        String action = (String) json.get("action");

        if ("message".equals(action)) {
            String content = (String) json.get("content");
            if (content == null || content.trim().isEmpty()) {
                return;
            }
            // 调用 DeepSeek 流式接口
            stream(content, session);
        }
        resetHeartbeat(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        cancelHeartbeat(session);
    }

    // ------------------ 流式调用 DeepSeek ------------------
    private void stream(String userMessage, WebSocketSession session) {
        Map<String, Object> body = Map.of(
                "model", "deepseek-chat",
                "messages", List.of(Map.of("role", "user", "content", userMessage)),
                "stream", true
        );

        WebClient client = WebClient.builder()
                .baseUrl("https://api.deepseek.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();

        // 用于保证消息顺序的锁
        final Object lock = new Object();
        // 累积完整回复（可选，不需要保存时可忽略）
        StringBuilder fullResponse = new StringBuilder();

        client.post()
                .uri("/v1/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .subscribe(
                        sse -> {
                            try {
                                String token = extractToken(sse);
                                if (token != null && session.isOpen()) {
                                    synchronized (lock) {
                                        fullResponse.append(token);
                                        session.sendMessage(new TextMessage(token));
                                    }
                                }
                            } catch (Exception e) {
                                // 忽略解析异常，继续接收
                            }
                        },
                        error -> {
                            // 错误处理：发送错误提示给前端
                            try {
                                if (session.isOpen()) {
                                    session.sendMessage(new TextMessage("抱歉，AI 服务暂时不可用，请稍后重试。"));
                                }
                            } catch (IOException ignored) {}
                        },
                        () -> {
                            // 流传输完成，可发送结束标记（前端不需要额外处理，仅用于日志）
                            System.out.println("Stream finished for session " + session.getId());
                        }
                );
    }

    // 提取 SSE 中的 token，返回 null 表示非内容行或结束标记
    private String extractToken(String sseLine) {
        if (sseLine == null || !sseLine.startsWith("data: ")) {
            return null;
        }
        String json = sseLine.substring(6).trim();
        if ("[DONE]".equals(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            JsonNode choices = node.path("choices");
            if (choices.isArray() && !choices.isEmpty()) {
                JsonNode delta = choices.get(0).path("delta").path("content");
                if (!delta.isNull()) {
                    return delta.asText();
                }
            }
        } catch (Exception e) {
            // 无法解析时忽略
        }
        return null;
    }

    // ------------------ 心跳管理 ------------------
    private void scheduleHeartbeat(WebSocketSession session) {
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            try {
                if (session.isOpen()) {
                    System.out.println("Heartbeat timeout, closing session " + session.getId());
                    session.close(CloseStatus.SESSION_NOT_RELIABLE);
                }
            } catch (IOException e) {
                // ignore
            }
        }, 60, TimeUnit.SECONDS);
        session.getAttributes().put("heartbeatFuture", future);
    }

    private void resetHeartbeat(WebSocketSession session) {
        ScheduledFuture<?> oldFuture = (ScheduledFuture<?>) session.getAttributes().get("heartbeatFuture");
        if (oldFuture != null && !oldFuture.isDone()) {
            oldFuture.cancel(false);
        }
        scheduleHeartbeat(session);
    }

    private void cancelHeartbeat(WebSocketSession session) {
        ScheduledFuture<?> future = (ScheduledFuture<?>) session.getAttributes().get("heartbeatFuture");
        if (future != null && !future.isDone()) {
            future.cancel(false);
        }
    }
}