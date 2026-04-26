package pers.eloyhere.lively.component.websocket;

import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import pers.eloyhere.lively.entity.chat.Chat;
import pers.eloyhere.lively.service.chat.ChatService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component("chatHandshakeInterceptor")
public class ChatHandshakeInterceptor implements HandshakeInterceptor {

    private final ChatService chatService;

    public ChatHandshakeInterceptor(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        Authentication authentication = (Authentication) attributes.get("authentication");
        if(Objects.isNull(authentication) || !authentication.isAuthenticated()){
            return false;
        }
        if(Objects.isNull(request.getAttributes().get("identity"))){
            Chat chat = new Chat();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.ss");
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
            chat.setChatName(formatter.format(now));
            chat.setDescription("新对话");
            chat = chatService.insert(chat);
            attributes.put("chat", chat);
            return true;
        }
        try{
            String raw = String.valueOf(request.getAttributes().get("identity"));
            UUID identity = UUID.fromString(raw);
            Optional<Chat> optional = chatService.findById(identity);
            optional.ifPresent(chat -> attributes.put("chat", chat));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }
}
