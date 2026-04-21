package pers.eloyhere.lively.websocket;


import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class MonitorWebsocket extends TextWebSocketHandler {

    private static Runtime runtime = Runtime.getRuntime();

    private final ObjectMapper mapper = new ObjectMapper();

    private final ObjectWriter writer = mapper.writerFor(TreeMap.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        TextMessage message = new TextMessage("Created.");
        session.sendMessage(message);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String content = new String(message.asBytes());
        TreeMap<String, Long> map = new TreeMap<>();
        map.put("processors", (long) runtime.availableProcessors());
        map.put("maxMemory", runtime.maxMemory());
        map.put("freeMemory", runtime.freeMemory());
        map.put("totalMemory", runtime.totalMemory());
        TextMessage reply = new TextMessage(writer.writeValueAsBytes(map));
        session.sendMessage(reply);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}
