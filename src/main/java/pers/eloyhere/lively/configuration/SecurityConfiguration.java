package pers.eloyhere.lively.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import pers.eloyhere.lively.authentication.entry.InvalidateAuthenticationEntryPoint;
import pers.eloyhere.lively.authentication.filter.LivelyUsernamePasswordAuthenticationFilter;
import pers.eloyhere.lively.authentication.manager.RequestAuthorizationManager;
import pers.eloyhere.lively.authentication.provider.UsernamePasswordAuthenticationProvider;
import pers.eloyhere.lively.service.consumer.ConsumerService;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityContextRepository securityContextRepository(){
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public AuthenticationManager authenticationManager(ConsumerService consumerService){
        return new ProviderManager(
                new UsernamePasswordAuthenticationProvider(consumerService)
        );
    }

    @Bean
    public SecurityFilterChain http(
            final HttpSecurity security,
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository,
            InvalidateAuthenticationEntryPoint invalidateAuthenticationEntryPoint
    ){
        return security.authorizeHttpRequests((request) ->
                        request.requestMatchers("/", "/**.*", "/static/**", "/authentication/**").permitAll().anyRequest().permitAll()//.anyRequest().access(new RequestAuthorizationManager())
                ).addFilterBefore(new LivelyUsernamePasswordAuthenticationFilter(authenticationManager, securityContextRepository), UsernamePasswordAuthenticationFilter.class).exceptionHandling((exception) -> exception.authenticationEntryPoint(invalidateAuthenticationEntryPoint))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/authentication/expire")
                .maximumSessions(4)
                .expiredUrl("/authentication/expire")).csrf(AbstractHttpConfigurer::disable).build();
    }
}
