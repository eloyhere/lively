package pers.eloyhere.lively.component;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pers.eloyhere.lively.entity.consumer.Authority;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.entity.consumer.Role;
import pers.eloyhere.lively.repository.consumer.AuthorityRepository;
import pers.eloyhere.lively.repository.consumer.ConsumerRepository;
import pers.eloyhere.lively.repository.consumer.RoleRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Order(-42148519)
@Component
public class DataInitializer implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;

    private final RoleRepository roleRepository;

    private final ConsumerRepository consumerRepository;

    public DataInitializer(AuthorityRepository authorityRepository, RoleRepository roleRepository, ConsumerRepository consumerRepository) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
        this.consumerRepository = consumerRepository;
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        List<Authority> authorities = authorityRepository.saveAllAndFlush(
                Stream.of("select", "insert", "update", "delete", "count")
                .map((a) -> {
                    Authority authority = new Authority();
                    authority.setAuthority(a);
                    authority.setDescription(a);
                    return authority;
                }).toList());
        List<Authority> modules = authorityRepository.saveAllAndFlush(
                Stream.of("token", "consumer", "book", "chapter", "chat", "message", "authority", "invitation", "role", "announcement")
                .map((a) -> {
                    Authority authority = new Authority();
                    authority.setAuthority(a);
                    authority.setDescription(a);
                    return authority;
                }).toList());
        List<Role> roles = roleRepository.saveAllAndFlush(Stream.of("administrator", "consumer", "guest").map((name) -> {
            Role role = new Role();
            role.setName(name);
            role.setDescription(name);
            return role;
        }).toList());

        Consumer administrator = new Consumer();
        administrator.setNickname("administrator");
        administrator.setAvatar("http://localhost:8080/smile.png");
        administrator.setPassword(passwordEncoder.encode("z123."));
        administrator.setUsername("administrator");
        roles.forEach((role) -> {
            if(role.getName().contentEquals(administrator.getUsername())){
                administrator.getRoles().add(role);
            }
        });
        consumerRepository.saveAndFlush(administrator);
    }
}
