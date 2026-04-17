package pers.eloyhere.lively.component;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
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

        Runnable createRoot = () -> {
            Authority allDirectories = authorityRepository.saveAndFlush(new Authority("/**"));
            Authority all = authorityRepository.saveAndFlush(new Authority("/**/**"));

            Role rawRootRole = new Role();
            rawRootRole.setName("root");
            rawRootRole.add(allDirectories);
            rawRootRole.add(all);
            Role rootRole = roleRepository.saveAndFlush(rawRootRole);

            Consumer consumer = new Consumer();
            consumer.add(rootRole);
            consumer.setPassword(passwordEncoder.encode("z123."));
            consumer.setAvatar("123");
            consumer.setNickname("root");
            consumer.setUsername("root");
            consumerRepository.saveAndFlush(consumer);
        };
        createRoot.run();
    }
}
