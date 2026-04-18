package pers.eloyhere.lively.controller;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import pers.eloyhere.lively.annotation.Authenticated;
import pers.eloyhere.lively.annotation.Everyone;
import pers.eloyhere.lively.annotation.Guest;
import pers.eloyhere.lively.annotation.Unauthenticated;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.entity.consumer.Invitation;
import pers.eloyhere.lively.entity.consumer.Token;
import pers.eloyhere.lively.service.authentication.LivelyPersistentTokenBasedRememberMeServices;
import pers.eloyhere.lively.service.consumer.ConsumerService;
import pers.eloyhere.lively.service.consumer.InvitationService;
import pers.eloyhere.lively.service.consumer.TokenService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("authentication")
class AuthenticationController {

    private final SecurityContextRepository repository;

    private final ConsumerService consumerService;

    private final TokenService tokenService;

    private final InvitationService invitationService;

    private final AuthenticationManager authenticationManager;

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    private final LivelyPersistentTokenBasedRememberMeServices livelyPersistentTokenBasedRememberMeServices;

    AuthenticationController(SecurityContextRepository repository, ConsumerService consumerService, TokenService tokenService, InvitationService invitationService, AuthenticationManager authenticationManager, HttpServletRequest request, HttpServletResponse response, LivelyPersistentTokenBasedRememberMeServices livelyPersistentTokenBasedRememberMeServices) {
        this.repository = repository;
        this.consumerService = consumerService;
        this.tokenService = tokenService;
        this.invitationService = invitationService;
        this.authenticationManager = authenticationManager;
        this.request = request;
        this.response = response;
        this.livelyPersistentTokenBasedRememberMeServices = livelyPersistentTokenBasedRememberMeServices;
    }

    @Everyone
    @GetMapping("success")
    public ResponseEntity<Authentication> success(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return ResponseEntity.ok(authentication);
    }

    @Everyone
    @GetMapping("auto")
    public ResponseEntity<Authentication> auto(){
        try{
            Authentication authentication = livelyPersistentTokenBasedRememberMeServices.autoLogin(request, response);
            SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
            SecurityContext context = strategy.getContext();
            context.setAuthentication(authentication);
            strategy.setContext(context);
            repository.saveContext(context, request, response);
            return ResponseEntity.ok(authentication);
        }catch (AuthenticationException exception){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }catch (Exception exception){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("failure")
    public ResponseEntity<Authentication> failure(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return ResponseEntity.ok(authentication);
    }

    @Authenticated
    @GetMapping("identity")
    public ResponseEntity<Authentication> identity(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return ResponseEntity.ok(authentication);
    }

    @Unauthenticated
    @PostMapping(value = "register")
    public ResponseEntity<Authentication> register(Consumer consumer, @RequestParam String invitation){
        Invitation example = new Invitation();
        example.setCode(invitation);
        Optional<Invitation> entity = invitationService.findOneBy(example);
        if(entity.isPresent()){
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
            Invitation value = entity.get();
            value.setLock(now.plusYears(100));
            invitationService.update(value);

            Consumer trust = consumerService.insert(consumer);
            UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.authenticated(consumer, consumer.getPassword(), trust.getAuthorities());
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(token);
            SecurityContextHolder.setContext(context);
            SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
            strategy.setContext(context);
            repository.saveContext(context, request, response);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.notFound().build();
    }

    @Authenticated
    @GetMapping(value = "expire")
    public ResponseEntity<String> expire(){
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "lock")
    public ResponseEntity<String> lock(){
        return ResponseEntity.ok(null);
    }

    @GetMapping(value = "ban")
    public ResponseEntity<String> ban(){
        return ResponseEntity.ok(null);
    }
}
