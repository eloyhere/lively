package pers.eloyhere.lively.authentication.filter;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import pers.eloyhere.lively.entity.consumer.Consumer;

public class LivelyAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, Consumer> {

    @Override
    @Nonnull
    public Consumer buildDetails(HttpServletRequest context) {
        String username = context.getParameter("username");
        String password = context.getParameter("password");
        Consumer consumer = new Consumer();
        consumer.setUsername(username);
        consumer.setPassword(password);
        return consumer;
    }
}
