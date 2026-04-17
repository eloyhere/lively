package pers.eloyhere.lively.authentication.provider;

import jakarta.annotation.Nonnull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.service.consumer.ConsumerService;

import java.util.Objects;

public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final ConsumerService consumerService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsernamePasswordAuthenticationProvider(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        Consumer consumer = consumerService.loadUserByUsername(username);
        if(passwordEncoder.matches(password, consumer.getPassword())){
            return UsernamePasswordAuthenticationToken.authenticated(consumer, password, consumer.getAuthorities());
        }
        return UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    }

    @Override
    public boolean supports(@Nonnull final Class<?> authentication) {
        return Objects.equals(authentication, UsernamePasswordAuthenticationToken.class);
    }
}
