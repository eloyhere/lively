package pers.eloyhere.lively.service.authentication;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import pers.eloyhere.lively.service.consumer.ConsumerService;
import pers.eloyhere.lively.service.consumer.TokenService;

import java.util.UUID;

public class FixedPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {

    public FixedPersistentTokenBasedRememberMeServices(ConsumerService consumerService, TokenService tokenService) {
        super(String.valueOf(UUID.randomUUID()), consumerService, tokenService);
        this.setSeriesLength(128);
        this.setAlwaysRemember(true);
        this.setTokenLength(128);
        this.setTokenValiditySeconds(60*60*24*7);
        this.setParameter("Authorization");
        this.setUseSecureCookie(true);
        this.setCookieName("Authorization");
    }
}
