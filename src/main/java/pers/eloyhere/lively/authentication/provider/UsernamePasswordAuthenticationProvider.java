package pers.eloyhere.lively.authentication.provider;

import jakarta.annotation.Nonnull;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        if(Objects.isNull(username) || Objects.isNull(password) || username.isEmpty() || password.isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Authentication Failed");
        }
        UserDetails user = consumerService.loadUserByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if(passwordEncoder.matches(password, user.getPassword())){
            return UsernamePasswordAuthenticationToken.authenticated(user, password, user.getAuthorities());
        }
        throw new BadCredentialsException("Invalid username or password");
    }

    @Override
    public boolean supports(@Nonnull final Class<?> authentication) {
        return Objects.equals(authentication, UsernamePasswordAuthenticationToken.class);
    }
}
