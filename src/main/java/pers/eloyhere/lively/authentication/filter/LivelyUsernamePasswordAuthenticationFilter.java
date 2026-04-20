package pers.eloyhere.lively.authentication.filter;

import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import pers.eloyhere.lively.authentication.filter.handler.LivelyAuthenticationFailureHandler;
import pers.eloyhere.lively.authentication.filter.handler.LivelyAuthenticationSuccessHandler;
import pers.eloyhere.lively.entity.consumer.Consumer;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;

public class  LivelyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final LivelyAuthenticationDetailsSource livelyAuthenticationDetailsSource = new LivelyAuthenticationDetailsSource();

    private final ConcurrentHashMap<String, Long> repeat = new ConcurrentHashMap<>();

    public LivelyUsernamePasswordAuthenticationFilter(ProviderManager providerManager){
        super(providerManager);
        setFilterProcessesUrl("/authentication/login");
        setUsernameParameter("username");
        setPasswordParameter("password");
        setPostOnly(true);
    }

    @Nonnull
    @Override
    public Authentication attemptAuthentication(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response) throws AuthenticationException {
        String username = this.obtainUsername(request);
        String password = this.obtainPassword(request);
        if(Objects.isNull(username) || username.isEmpty() || Objects.isNull(password) ||  password.isEmpty()){
            throw new AuthenticationCredentialsNotFoundException("Authentication Failed");
        }
        HttpSession session = request.getSession(false);
        String sessionId = Objects.nonNull(session) ? session.getId() : null;
        String fingerprint = String.valueOf(request.getRemoteUser())
                .concat(String.valueOf(sessionId))
                .concat(username)
                .concat(Stream.of("X-Forwarded-For",
                        "Proxy-Client-IP",
                        "WL-Proxy-Client-IP",
                        "HTTP_CLIENT_IP",
                        "HTTP_X_FORWARDED_FOR"
                ).map(request::getHeader).filter(Objects::nonNull).reduce("", String::concat))
                .concat(String.valueOf(request.getHeader("User-Agent")));
        long now = System.currentTimeMillis();
        if(repeat.containsKey(fingerprint) && (now - repeat.get(fingerprint)) <= 10000L){
            throw new LockedException("Authentication locked.");
        }
        repeat.put(fingerprint, now);
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void setDetails(@Nonnull HttpServletRequest request, @Nonnull UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.livelyAuthenticationDetailsSource.buildDetails(request));
    }
}
