package pers.eloyhere.lively.service.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Service;
import pers.eloyhere.lively.service.consumer.ConsumerService;
import pers.eloyhere.lively.service.consumer.TokenService;

@Service("livelyPersistentTokenBasedRememberMeServices")
public class LivelyPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {

    public LivelyPersistentTokenBasedRememberMeServices(
            @Value("${lively.security.remember-me.key:lively-default-remember-me-key}") String rememberMeKey,
            ConsumerService consumerService,
            TokenService tokenService) {
        super(rememberMeKey, consumerService, tokenService);
        this.setSeriesLength(128);
        this.setAlwaysRemember(true);
        this.setTokenLength(128);
        this.setTokenValiditySeconds(60*60*24*7);
        this.setParameter("remember");
        this.setUseSecureCookie(true);
        this.setCookieName("remember");
    }
}
