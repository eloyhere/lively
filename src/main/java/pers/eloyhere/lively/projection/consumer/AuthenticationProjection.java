package pers.eloyhere.lively.projection.consumer;

import jakarta.annotation.Nonnull;
import org.springframework.security.core.userdetails.UserDetails;
import pers.eloyhere.lively.entity.consumer.Token;
import pers.eloyhere.lively.projection.BaseProjection;

import java.util.Set;

public interface AuthenticationProjection extends BaseProjection, UserDetails {

    @Nonnull
    public String getUsername();

    public String getPassword();

    public Set<Token> getTokens();

    public String getAvatar();
}
