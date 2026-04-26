package pers.eloyhere.lively.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import pers.eloyhere.lively.component.websocket.AuthenticationHandshakeInterceptor;
import pers.eloyhere.lively.websocket.AuthenticationWebsocket;
import pers.eloyhere.lively.websocket.MonitorWebsocket;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final AuthenticationHandshakeInterceptor authenticationHandshakeInterceptor;

    public WebSocketConfiguration(AuthenticationHandshakeInterceptor authenticationHandshakeInterceptor) {
        this.authenticationHandshakeInterceptor = authenticationHandshakeInterceptor;
    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(1);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MonitorWebsocket(), "/websocket/monitor")
                .addInterceptors(authenticationHandshakeInterceptor)
                .setAllowedOrigins("http://localhost", "http://127.0.0.1");
        registry.addHandler(new AuthenticationWebsocket(), "/websocket/authentication")
                .addInterceptors(authenticationHandshakeInterceptor)
                .setAllowedOrigins("http://localhost", "http://127.0.0.1");
    }
}
