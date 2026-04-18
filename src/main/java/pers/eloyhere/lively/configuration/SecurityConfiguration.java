package pers.eloyhere.lively.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pers.eloyhere.lively.authentication.entry.InvalidateAuthenticationEntryPoint;
import pers.eloyhere.lively.authentication.filter.LivelyUsernamePasswordAuthenticationFilter;
import pers.eloyhere.lively.authentication.filter.handler.LivelyAuthenticationFailureHandler;
import pers.eloyhere.lively.authentication.filter.handler.LivelyAuthenticationSuccessHandler;
import pers.eloyhere.lively.authentication.manager.RequestAuthorizationManager;
import pers.eloyhere.lively.authentication.provider.UsernamePasswordAuthenticationProvider;
import pers.eloyhere.lively.service.consumer.ConsumerService;

import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityContextRepository securityContextRepository(){
        HttpSessionSecurityContextRepository repository = new HttpSessionSecurityContextRepository();
        repository.setDisableUrlRewriting(true);
        return repository;
    }

    @Bean
    public ProviderManager providerManager(ConsumerService consumerService){
        return new ProviderManager(
                new UsernamePasswordAuthenticationProvider(consumerService),
                new RememberMeAuthenticationProvider("remember"),
                new AnonymousAuthenticationProvider("anonymous")
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
    InvalidateAuthenticationEntryPoint invalidateAuthenticationEntryPoint(){
        return new InvalidateAuthenticationEntryPoint();
    }

    @Bean
    public SecurityFilterChain http(
            final HttpSecurity security,
            ProviderManager providerManager,
            RememberMeServices rememberMeServices,
            CorsConfigurationSource corsConfigurationSource,
            SecurityContextRepository securityContextRepository,
            InvalidateAuthenticationEntryPoint invalidateAuthenticationEntryPoint
    ){
        AuthenticationSuccessHandler authenticationSuccessHandler = new LivelyAuthenticationSuccessHandler(securityContextRepository);
        AuthenticationFailureHandler authenticationFailureHandler = new LivelyAuthenticationFailureHandler();
        LivelyUsernamePasswordAuthenticationFilter livelyUsernamePasswordAuthenticationFilter = new LivelyUsernamePasswordAuthenticationFilter(providerManager);
        livelyUsernamePasswordAuthenticationFilter.setRememberMeServices(rememberMeServices);
        livelyUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        livelyUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return security.cors((cors) -> cors.configurationSource(corsConfigurationSource)).authorizeHttpRequests((request) ->
                    request.requestMatchers("/", "/**", "/**.*").permitAll().anyRequest().access(new RequestAuthorizationManager())
                ).anonymous(AbstractHttpConfigurer::disable)
                .securityContext((context) -> context.requireExplicitSave(true).securityContextRepository(securityContextRepository))
                .rememberMe((remember) -> remember.rememberMeServices(rememberMeServices))
                .addFilterAt(livelyUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).exceptionHandling((exception) -> exception.authenticationEntryPoint(invalidateAuthenticationEntryPoint))
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .invalidSessionStrategy((request, response) -> {
                        SecurityContextHolder.clearContext();
                        response.setStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value());
                    })
                    .maximumSessions(4)
                    .maxSessionsPreventsLogin(false)
                    .expiredSessionStrategy(event -> {
                        SecurityContextHolder.clearContext();
                        HttpServletResponse response = event.getResponse();
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    })
                )
                .logout((logout) -> logout.logoutUrl("/authentication/logout")
                    .addLogoutHandler(new SecurityContextLogoutHandler())
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("Authentication", "remember", "JSESSIONID")
                    .permitAll()
                ).csrf(AbstractHttpConfigurer::disable).build();
    }
}
