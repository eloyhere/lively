package pers.eloyhere.lively.service.consumer;

import io.netty.handler.codec.base64.Base64Decoder;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.entity.consumer.Token;
import pers.eloyhere.lively.repository.consumer.ConsumerRepository;
import pers.eloyhere.lively.repository.consumer.TokenRepository;
import pers.eloyhere.lively.service.BaseService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service("tokenService")
public class TokenService extends BaseService<Token, TokenRepository> implements PersistentTokenRepository {
    private final ConsumerRepository consumerRepository;
    private final TokenRepository repository;

    public TokenService(TokenRepository repository, ConsumerRepository consumerRepository) {
        super(repository);
        this.consumerRepository = consumerRepository;
        this.repository = repository;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        Consumer consumer = consumerRepository.findByUsername(token.getUsername()).orElseThrow(()-> new NoSuchElementException("Consumer not found"));
        Token entity = Token.fromPersistentRememberMeToken(token, consumer);
        this.insert(entity);
    }

    @Override
    public void updateToken(@NonNull String series, @NonNull String tokenValue, @NonNull Date lastUsed) {
        repository.findBySeries(series).map((entity)-> {
            entity.setToken(tokenValue);
            entity.setLast(lastUsed);
            this.update(entity);
            return entity;
        });
    }

    @Transactional
    public Authentication authenticateByToken(String remember) throws AuthenticationException {
        if(Objects.isNull(remember) || remember.isBlank()){
            return UsernamePasswordAuthenticationToken.unauthenticated(remember, null);
        }
        try {
            System.out.println("===========remember "+remember);
            Base64.Decoder decoder = Base64.getDecoder();
            String decoded = new String(decoder.decode(remember), StandardCharsets.UTF_8);
            String[] parts = decoded.split(":", 2);
            if(parts.length != 2){
                System.out.println("===========remember length mismatch "+remember);
                return UsernamePasswordAuthenticationToken.unauthenticated(remember, null);
            }
            String series = parts[0];
            String token = parts[1];
            return this.repository.findBySeries(series).map((entity) -> {
                if(!entity.getToken().contentEquals(token)){
                    this.deleteById(entity.getId());
                    System.out.println("===========remember stole "+remember);
                    throw new CookieTheftException(token);
                }
                entity.setToken(String.valueOf(UUID.randomUUID()));
                entity.setLast(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                Token saved = this.repository.saveAndFlush(entity);
                return UsernamePasswordAuthenticationToken.authenticated(saved.getConsumer().getUsername(), remember, saved.getConsumer().getAuthorities());
            }).orElse(UsernamePasswordAuthenticationToken.unauthenticated(remember, null));
        } catch (Exception e) {
            System.out.println("===========remember exception "+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(@NonNull String seriesId) {
        return repository.findBySeries(seriesId).map(Token::toPersistentRememberMeToken).orElse(null);
    }

    @Override
    public void removeUserTokens(@NonNull String username) {
        this.repository.deleteByUsername(username);
    }
}
