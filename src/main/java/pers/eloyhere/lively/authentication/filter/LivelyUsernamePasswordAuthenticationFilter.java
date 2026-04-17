package pers.eloyhere.lively.authentication.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import pers.eloyhere.lively.authentication.filter.handler.LivelyAuthenticationFailureHandler;
import pers.eloyhere.lively.authentication.filter.handler.LivelyAuthenticationSuccessHandler;

public class LivelyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public LivelyUsernamePasswordAuthenticationFilter(final AuthenticationManager manager, SecurityContextRepository securityContextRepository){
        super(manager);
        setFilterProcessesUrl("/authentication/login");
        setUsernameParameter("username");
        setPasswordParameter("password");
        setPostOnly(true);
        setAuthenticationSuccessHandler(new LivelyAuthenticationSuccessHandler(securityContextRepository));
        setAuthenticationFailureHandler(new LivelyAuthenticationFailureHandler());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return super.attemptAuthentication(request, response);
    }
}
