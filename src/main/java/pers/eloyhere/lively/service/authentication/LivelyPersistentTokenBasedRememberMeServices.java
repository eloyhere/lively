package pers.eloyhere.lively.service.authentication;

import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Service;
import pers.eloyhere.lively.service.consumer.ConsumerService;
import pers.eloyhere.lively.service.consumer.TokenService;

import java.util.UUID;

@Service("livelyPersistentTokenBasedRememberMeServices")
public class LivelyPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {

    public LivelyPersistentTokenBasedRememberMeServices(ConsumerService consumerService, TokenService tokenService) {
        super(String.valueOf(UUID.randomUUID()), consumerService, tokenService);
        this.setSeriesLength(128);
        this.setAlwaysRemember(true);
        this.setTokenLength(128);
        this.setTokenValiditySeconds(60*60*24*7);
        this.setParameter("remember");
        this.setUseSecureCookie(true);
        this.setCookieName("remember");
    }
}
