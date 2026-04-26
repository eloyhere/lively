package pers.eloyhere.lively.websocket;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pers.eloyhere.lively.entity.consumer.Consumer;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentSkipListMap;

public class AuthenticationWebsocket  extends TextWebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    private final ObjectWriter writer = mapper.writerFor(Authentication.class);

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Authentication authentication = (Authentication) session.getAttributes().get("authentication");
        TextMessage reply = new TextMessage(writer.writeValueAsString(authentication));
        session.sendMessage(reply);
    }
}
