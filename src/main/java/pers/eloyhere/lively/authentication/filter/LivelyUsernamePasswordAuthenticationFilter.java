package pers.eloyhere.lively.authentication.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

public class LivelyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final LivelyAuthenticationDetailsSource livelyAuthenticationDetailsSource = new LivelyAuthenticationDetailsSource();

    public LivelyUsernamePasswordAuthenticationFilter(ProviderManager providerManager){
        super(providerManager);
        setFilterProcessesUrl("/authentication/login");
        setUsernameParameter("username");
        setPasswordParameter("password");
        setPostOnly(true);
    }

    @Override
    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.livelyAuthenticationDetailsSource.buildDetails(request));
    }
}
