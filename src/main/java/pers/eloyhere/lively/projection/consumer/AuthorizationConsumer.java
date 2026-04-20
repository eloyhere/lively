package pers.eloyhere.lively.projection.consumer;

import org.springframework.security.core.userdetails.UserDetails;
import pers.eloyhere.lively.entity.consumer.Authority;
import pers.eloyhere.lively.entity.consumer.Role;
import pers.eloyhere.lively.projection.BaseProjection;

import java.util.Set;
import java.util.stream.Collectors;

public interface AuthorizationConsumer extends BaseProjection, UserDetails {

    public String getUsername();

    public String getPassword();

    public Set<Role> getRoles();

    public default Set<Authority>  getAuthorities(){
        return this.getRoles().stream().flatMap((role) -> role.getAuthorities().stream()).collect(Collectors.toSet());
    }
}
