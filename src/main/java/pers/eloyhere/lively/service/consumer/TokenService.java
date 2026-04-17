package pers.eloyhere.lively.service.consumer;

import org.jspecify.annotations.NonNull;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.entity.consumer.Token;
import pers.eloyhere.lively.repository.consumer.ConsumerRepository;
import pers.eloyhere.lively.repository.consumer.TokenRepository;
import pers.eloyhere.lively.service.BaseService;

import java.util.Date;
import java.util.NoSuchElementException;

@Service("tokenService")
public class TokenService extends BaseService<Token, TokenRepository> implements PersistentTokenRepository {
    private final ConsumerRepository consumerRepository;
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository repository, ConsumerRepository consumerRepository,
                        TokenRepository tokenRepository) {
        super(repository);
        this.consumerRepository = consumerRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        Consumer consumer = consumerRepository.findByUsername(token.getUsername()).orElseThrow(()-> new NoSuchElementException("Consumer not found"));
        Token entity = Token.fromPersistentRememberMeToken(token, consumer);
        this.insert(entity);
    }

    @Override
    public void updateToken(@NonNull String series, @NonNull String tokenValue, @NonNull Date lastUsed) {
        tokenRepository.findBySeries(series).map((entity)-> {
            entity.setToken(tokenValue);
            entity.setLast(lastUsed);
            this.update(entity);
            return entity;
        });
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(@NonNull String seriesId) {
        return tokenRepository.findBySeries(seriesId).map(Token::toPersistentRememberMeToken).orElse(null);
    }

    @Override
    public void removeUserTokens(@NonNull String username) {
        Consumer consumer = consumerRepository.findByUsername(username).orElseThrow(()-> new NoSuchElementException("Consumer not found"));
        consumer.getTokens().forEach(this::deleteBy);
    }
}
