package pers.eloyhere.lively.authentication.manager;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatchers;
import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.function.Supplier;
import java.util.regex.PatternSyntaxException;

public class RequestAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public void verify(@NonNull Supplier<? extends @Nullable Authentication> supplier, @NonNull RequestAuthorizationContext object) throws AuthenticationCredentialsNotFoundException, AuthorizationDeniedException {
        AuthorizationResult authorizationResult = this.authorize(supplier, object);
        if(Objects.isNull(authorizationResult)){
            throw new AuthenticationCredentialsNotFoundException("Authentication credentials not found.");
        }
        if(!authorizationResult.isGranted()){
            throw new AuthorizationDeniedException("Permission denied.");
        }
    }

    @Override
    public @Nullable AuthorizationResult authorize(@NonNull Supplier<? extends @Nullable Authentication> supplier, RequestAuthorizationContext object) {
        Authentication authentication = supplier.get();
        if(Objects.isNull(object)){
            return new AuthorizationDecision(false);
        }
        HttpServletRequest request = object.getRequest();
        if(Objects.isNull(authentication) || !authentication.isAuthenticated()){
            return new AuthorizationDecision(false);
        }

        boolean permit = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).filter(Objects::nonNull).anyMatch((authority) -> {
            ArrayList<RequestMatcher> matchers = new ArrayList<>();
            matchers.add((r) -> r.getRequestURI().contentEquals(authority));
            matchers.add((r) -> r.getMethod().toLowerCase(Locale.ROOT).contentEquals(authority.toLowerCase(Locale.ROOT)));
            if(authority.startsWith("/")){
                matchers.add(PathPatternRequestMatcher.pathPattern(authority));
            }
            try{
                matchers.add(RegexRequestMatcher.regexMatcher(authority));
            }catch (PatternSyntaxException ignored){

            }
            OrRequestMatcher matcher = new OrRequestMatcher(matchers);
            return matcher.matches(request) && !PathPatternRequestMatcher.pathPattern("/authentication/**").matches(request);
        });
        return new AuthorizationDecision(permit);
    }
}
