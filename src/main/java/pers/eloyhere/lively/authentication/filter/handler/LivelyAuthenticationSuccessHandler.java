package pers.eloyhere.lively.authentication.filter.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.io.IOException;

public class LivelyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final SecurityContextRepository repository;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public LivelyAuthenticationSuccessHandler(SecurityContextRepository securityContextRepository) {
        this.repository = securityContextRepository;
    }

    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request, HttpServletResponse response, @NonNull Authentication authentication) throws IOException, ServletException {
        SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = strategy.createEmptyContext();
        context.setAuthentication(authentication);
        strategy.setContext(context);
        SecurityContextHolder.setContext(context);

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:80");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerFor(Authentication.class);
        response.getWriter().write(writer.writeValueAsString(authentication));

        repository.saveContext(context, request, response);
    }
}
