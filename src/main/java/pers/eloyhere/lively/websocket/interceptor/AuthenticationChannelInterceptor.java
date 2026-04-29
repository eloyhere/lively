package pers.eloyhere.lively.websocket.interceptor;

import org.jspecify.annotations.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("authenticationChannelInterceptor")
public class AuthenticationChannelInterceptor implements ChannelInterceptor {

    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {

            Map<String, Object> attributes = accessor.getSessionAttributes();
            if (attributes != null) {
                Authentication authentication = (Authentication) attributes.get("authentication");
                if (authentication != null && authentication.isAuthenticated()) {
                    accessor.setUser(authentication);
                }
            }
        }
        return message;
    }
}
