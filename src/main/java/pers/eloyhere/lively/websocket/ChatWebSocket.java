package pers.eloyhere.lively.websocket;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pers.eloyhere.lively.entity.chat.Chat;
import pers.eloyhere.lively.entity.chat.ChatRole;
import pers.eloyhere.lively.entity.chat.Message;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.service.chat.ChatService;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Component("chatWebSocket")
public class ChatWebSocket extends TextWebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    private final ObjectWriter writer = mapper.writerFor(Map.class);

    private final ScheduledExecutorService scheduler;

    private final ChatService chatService;

    public ChatWebSocket(ScheduledExecutorService scheduler, ChatService chatService) {
        this.scheduler = scheduler;
        this.chatService = chatService;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        cancelHeartbeat(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Authentication authentication = (Authentication) session.getAttributes().get("authentication");
        if(Objects.isNull(authentication) || !authentication.isAuthenticated() || Objects.isNull(session.getAttributes().get("chat"))){
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }
        scheduleHeartbeat(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        Message receive = new Message();
        receive.setRole(ChatRole.User);
        receive.setContent(message.getPayload());
        Chat chat = (Chat) session.getAttributes().get("chat");
        chat.add(receive);
        chatService.update(chat);

        chatService.stream(chat, message.getPayload(), session);
        resetHeartbeat(session);
    }

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
