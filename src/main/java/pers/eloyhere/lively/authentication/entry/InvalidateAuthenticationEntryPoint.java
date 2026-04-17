package pers.eloyhere.lively.authentication.entry;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class InvalidateAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull AuthenticationException authenticationException) throws IOException, ServletException {
        if (authenticationException instanceof InsufficientAuthenticationException || authenticationException.getCause() instanceof SessionAuthenticationException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ResponseEntity<String> entity = new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writerFor(ResponseEntity.class);
            PrintWriter printWriter = response.getWriter();
            writer.writeValue(printWriter, entity);
        }
    }
}
