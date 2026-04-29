package pers.eloyhere.lively.service.consumer;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.repository.consumer.ConsumerRepository;
import pers.eloyhere.lively.repository.consumer.RoleRepository;
import pers.eloyhere.lively.service.BaseService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service("consumerService")
public class ConsumerService extends BaseService<Consumer, ConsumerRepository> implements UserDetailsManager {

    private final RoleRepository roleRepository;

    private final SessionRegistry sessionRegistry;

    public ConsumerService(ConsumerRepository repository, SessionRegistry sessionRegistry, RoleRepository roleRepository) {
        super(repository);
        this.sessionRegistry = sessionRegistry;
        this.roleRepository = roleRepository;
    }

    public void kickBy(final Consumer example){
        if(Objects.nonNull(example)){
            List<Object> principals = sessionRegistry.getAllPrincipals();
            Optional<Consumer> optional = this.findOneBy(example);
            optional.ifPresent((consumer) -> {
                for(Object principal : principals){
                    if(principal instanceof UserDetails u && Objects.equals(u.getUsername(), consumer.getUsername())){
                        List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                        for (SessionInformation session : sessions) {
                            session.expireNow();
                        }
                    }
                }
            });

        }
    }

    @Override
    public void createUser(@Nullable final UserDetails user) {
        if(Objects.nonNull(user) && user instanceof Consumer consumer){
            this.insert(consumer);
        }
    }

    public Optional<Consumer> addRole(@Nonnull final UUID consumer, @Nonnull final UUID role){
        return this.repository.findById(consumer).flatMap((c) -> {
            return this.roleRepository.findById(role).map((r) -> {
                c.add(r);
                return this.repository.saveAndFlush(c);
            });
        });
    }

    public Optional<Consumer> removeRole(@Nonnull final UUID consumer, @Nonnull final UUID role){
        return this.repository.findById(consumer).flatMap((c) -> {
            return this.roleRepository.findById(role).map((r) -> {
                c.remove(r);
                return this.repository.saveAndFlush(c);
            });
        });
    }

    @Override
    public void updateUser(@Nullable final UserDetails user) {
        if(Objects.nonNull(user) && user instanceof Consumer consumer){
            this.update(consumer);
        }
    }

    @Override
    public void deleteUser(@Nullable final String username) {
        if(Objects.nonNull(username)){
            this.repository.deleteByUsername(username);
        }
    }

    @Nonnull
    public Optional<Consumer> findByUsername(@Nullable final String username){
        if(Objects.isNull(username)){
            return Optional.empty();
        }
        return this.repository.findByUsername(username);
    }

    @Override
    public void changePassword(@Nullable final String oldPassword, @Nullable final String newPassword) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(Objects.nonNull(authentication) && authentication.isAuthenticated()){
            if(Objects.isNull(oldPassword) || Objects.isNull(newPassword)){
                throw new IllegalArgumentException("Password could not be null.");
            }
            if (oldPassword.trim().isEmpty() || newPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("Password could not be blank.");
            }
            if (oldPassword.contentEquals(newPassword)) {
                throw new IllegalArgumentException("Old password could not equals new password.");
            }
            if(!passwordEncoder.matches(oldPassword, (String) authentication.getCredentials())){
                throw new BadCredentialsException("Incorrect old password.");
            }
            Consumer consumer = this.loadUserByUsername(authentication.getName());
            consumer.setPassword(passwordEncoder.encode(newPassword));
            this.update(consumer);
        }else{
            throw new AuthenticationCredentialsNotFoundException("Not authenticated yet.");
        }
    }

    @Override
    public boolean userExists(@Nullable final String username) {
        if(Objects.nonNull(username)){
            return this.repository.existsByUsername(username);
        }
        return false;
    }

    @Nonnull
    @Override
    public Consumer loadUserByUsername(@Nullable final String username) throws UsernameNotFoundException {
        if(Objects.nonNull(username)){
            return this.repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        }
        throw new UsernameNotFoundException("Null.");
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
    }



}
