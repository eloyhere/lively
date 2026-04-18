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
        Runnable createAnonymous = () -> {
            Collection<Authority> authorities = authorityRepository.saveAllAndFlush(Stream.of("/", "/**", "/**.*", "/authentication/**").map(Authority::new).toList());
            Role anonymousRole = new Role();
            anonymousRole.setName("anonymous");
            anonymousRole.setDescription("匿名用户角色。");
            anonymousRole = roleRepository.saveAndFlush(anonymousRole);
            Consumer anonymousConsumer = new Consumer();
            anonymousConsumer.setUsername("anonymouse");
            anonymousConsumer.setNickname("anonymouse");
            anonymousConsumer.setAvatar("...");
            anonymousConsumer.setPassword(passwordEncoder.encode("..."));
            anonymousConsumer.add(anonymousRole);
            consumerRepository.saveAndFlush(anonymousConsumer);
        };
        createAnonymous.run();
        Runnable createRoot = () -> {
            Authority all = authorityRepository.saveAndFlush(new Authority("/**/**"));
            all.setDescription("多级路径通配符：能匹配任何深度的路径，包括 /users、/users/123、/users/123/profile等等，可以访问系统中的所有页面和接口，无限制。");

            Role rawRootRole = new Role();
            rawRootRole.setName("Administrator");
            rawRootRole.add(all);
            Role rootRole = roleRepository.saveAndFlush(rawRootRole);

            Consumer consumer = new Consumer();
            consumer.add(rootRole);
            consumer.setPassword(passwordEncoder.encode("z123."));
            consumer.setAvatar("http://localhost/smile.jpeg");
            consumer.setNickname("root");
            consumer.setUsername("root");
            consumerRepository.saveAndFlush(consumer);


        };
        createRoot.run();
    }
}
