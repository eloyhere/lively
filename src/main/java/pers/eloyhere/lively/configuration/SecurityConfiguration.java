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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pers.eloyhere.lively.authentication.entry.InvalidateAuthenticationEntryPoint;
import pers.eloyhere.lively.authentication.filter.LivelyUsernamePasswordAuthenticationFilter;
import pers.eloyhere.lively.authentication.manager.RequestAuthorizationManager;
import pers.eloyhere.lively.authentication.provider.UsernamePasswordAuthenticationProvider;
import pers.eloyhere.lively.service.consumer.ConsumerService;

import java.util.Arrays;

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
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8080",
                "http://127.0.0.1:8080"
        ));
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));
        configuration.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
