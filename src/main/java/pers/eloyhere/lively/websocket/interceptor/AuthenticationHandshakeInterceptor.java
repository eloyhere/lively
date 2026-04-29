package pers.eloyhere.lively.websocket.interceptor;

import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import pers.eloyhere.lively.service.authentication.LivelyPersistentTokenBasedRememberMeServices;

import java.util.Map;
import java.util.Objects;

@Component("authenticationHandshakeInterceptor")
public class AuthenticationHandshakeInterceptor  implements HandshakeInterceptor {

    private final SecurityContextRepository repository;

    private final LivelyPersistentTokenBasedRememberMeServices livelyPersistentTokenBasedRememberMeServices;

    public AuthenticationHandshakeInterceptor(SecurityContextRepository repository, LivelyPersistentTokenBasedRememberMeServices livelyPersistentTokenBasedRememberMeServices) {
        this.repository = repository;

        this.livelyPersistentTokenBasedRememberMeServices = livelyPersistentTokenBasedRememberMeServices;
    }


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        return this.process((ServletServerHttpRequest) request, (ServletServerHttpResponse) response, wsHandler, attributes);
    }

    public boolean process(ServletServerHttpRequest request, ServletServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        try{
            DeferredSecurityContext exist = repository.loadDeferredContext(request.getServletRequest());
            Authentication test = exist.get().getAuthentication();
            if(Objects.nonNull(test) && test.isAuthenticated()){
                attributes.put("authentication", test);
                return true;
            }
            Authentication authentication = livelyPersistentTokenBasedRememberMeServices.autoLogin(request.getServletRequest(), response.getServletResponse());
            SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
            SecurityContext context = strategy.createEmptyContext();
            context.setAuthentication(authentication);
            strategy.setContext(context);
            repository.saveContext(context, request.getServletRequest(), response.getServletResponse());
            if(Objects.nonNull(authentication) && authentication.isAuthenticated()){
                attributes.put("authentication", authentication);
                return true;
            }
            return false;
        } catch (Exception exception){
            System.out.println("                         "+exception.getMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }
}
