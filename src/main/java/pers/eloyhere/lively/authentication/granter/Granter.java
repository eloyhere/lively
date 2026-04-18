package pers.eloyhere.lively.authentication.granter;

import jakarta.annotation.Nonnull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pers.eloyhere.lively.entity.consumer.Authority;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.entity.consumer.Role;

import java.util.Objects;

@Component("granter")
public class Granter {

    @Nonnull
    private Authentication authentication(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if(Objects.isNull(authentication) || !authentication.isAuthenticated()){
            return UsernamePasswordAuthenticationToken.authenticated("guest", "guest", AuthorityUtils.createAuthorityList());
        }
        return authentication;
    }

    public boolean ownAdministrator(){
        String role = "administrator";
        Authentication authentication = this.authentication();
        Object principal = authentication.getPrincipal();
        if(principal instanceof Consumer consumer){
            return consumer.getRoles().stream().map(Role::getName).anyMatch((c) -> c.contentEquals(role));
        }
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).filter(Objects::nonNull).anyMatch((c) -> c.contentEquals(role));
    }

    public boolean ownRole(String role){
        Authentication authentication = this.authentication();
        Object principal = authentication.getPrincipal();
        if(principal instanceof Consumer consumer){
            return consumer.getRoles().stream().map(Role::getName).anyMatch((c) -> c.contentEquals(role));
        }
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).filter(Objects::nonNull).anyMatch((c) -> c.contentEquals(role));
    }

    public boolean ownAuthority(String authority) {
        Authentication authentication = this.authentication();
        Object principal = authentication.getPrincipal();
        if(principal instanceof Consumer consumer){
            return consumer.getRoles().stream().flatMap((role) -> role.getAuthorities().stream().map(Authority::getAuthority)).filter(Objects::nonNull).anyMatch((c) -> c.contentEquals(authority));
        }
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).filter(Objects::nonNull).anyMatch((c) -> c.contentEquals(authority));
    }
}
