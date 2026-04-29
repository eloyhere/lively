package pers.eloyhere.lively.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import pers.eloyhere.lively.service.chat.ChatService;
import pers.eloyhere.lively.websocket.ChatWebSocket;
import pers.eloyhere.lively.websocket.interceptor.AuthenticationHandshakeInterceptor;
import pers.eloyhere.lively.websocket.AuthenticationWebsocket;
import pers.eloyhere.lively.websocket.MonitorWebsocket;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final ChatService chatService;


    private final AuthenticationHandshakeInterceptor authenticationHandshakeInterceptor;

    public WebSocketConfiguration(ChatService chatService, AuthenticationHandshakeInterceptor authenticationHandshakeInterceptor) {
        this.chatService = chatService;
        this.authenticationHandshakeInterceptor = authenticationHandshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MonitorWebsocket(), "/websocket/monitor")
                .addInterceptors(authenticationHandshakeInterceptor)
                .setAllowedOrigins("http://localhost", "http://127.0.0.1");
        registry.addHandler(new AuthenticationWebsocket(), "/websocket/authentication")
                .addInterceptors(authenticationHandshakeInterceptor)
                .setAllowedOrigins("http://localhost", "http://127.0.0.1");
        registry.addHandler(new ChatWebSocket(), "/websocket/chat")
                .addInterceptors(authenticationHandshakeInterceptor)
                .setAllowedOrigins("http://localhost", "http://127.0.0.1");
    }
}
