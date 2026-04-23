package pers.eloyhere.lively.component;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pers.eloyhere.lively.entity.consumer.*;
import pers.eloyhere.lively.repository.consumer.AuthorityRepository;
import pers.eloyhere.lively.repository.consumer.ConsumerRepository;
import pers.eloyhere.lively.repository.consumer.RoleRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
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
        consumers(roles(authorities(), routes(), menus()));
    }

    private Map<String, Authority> authorities(){
        Map<String, Authority> map = new TreeMap<>();
        List<String> atomic = List.of("select", "insert", "update", "delete", "count");
        List<String> modules = List.of("book", "chapter", "message", "chat", "authority", "consumer", "invitation",
                "menu", "role", "route", "token", "announcement");
        authorityRepository.saveAllAndFlush(atomic.stream().map((a) -> {
            Authority authority = new Authority();
            authority.setAuthority(a);
            authority.setDescription(a);
            return authority;
        }).toList()).forEach((authority) -> map.put(authority.getAuthority(), authority));
        authorityRepository.saveAllAndFlush(modules.stream().filter((a)-> !map.containsKey(a)).map((a) -> {
            Authority authority = new Authority();
            authority.setAuthority(a);
            authority.setDescription(a);
            return authority;
        }).toList()).forEach((authority) -> map.put(authority.getAuthority(), authority));
        List<Authority> authorities = modules.stream().flatMap((a) -> {
            return atomic.stream().map((b)-> {
                String authorization = a.concat(":").concat(b);
               Authority authority = new Authority();
               authority.setAuthority(authorization);
               authority.setDescription(authorization);
               return authority;
            });
        }).toList();
        authorityRepository.saveAllAndFlush(authorities).forEach((authority) -> map.put(authority.getAuthority(), authority));
        return map;
    }

    private Map<String, Route> routes(){
        Map<String, Route> map = new TreeMap<>();
        Route route1 = new Route();
        route1.setPath("/");
        route1.setName("index");
        map.put(route1.getName(), route1);
        return map;
    }

    private Map<String, Menu> menus(){
        Map<String, Menu> map = new TreeMap<>();
        return map;
    }

    private Map<String, Role> roles(Map<String, Authority> authorities, Map<String, Route> routes, Map<String, Menu> menus){
        Map<String, Role> map = new TreeMap<>();
        roleRepository.saveAllAndFlush(Stream.of("administrator", "consumer", "guest").map((name)-> {
            Role role = new Role();
            role.setName(name);
            role.setDescription(name);
            switch (name){
                case "administrator" -> {
                    authorities.forEach((k, v) -> {
                        role.add(v);
                    });
                    routes.forEach((k, v) -> {
                        role.add(v);
                    });
                    menus.forEach((k, v) -> {
                        role.add(v);
                    });
                }
                case "consumer" -> {
                    List<String> white = List.of("book", "chapter", "chat", "message", "consumer", "announcement", "select", "count");
                    authorities.forEach((k, v) -> {
                        if(white.contains(k)){
                            role.add(v);
                        }
                    });
                    routes.forEach((k, v) -> {
                        if(white.contains(k)){
                            role.add(v);
                        }
                    });
                    menus.forEach((k, v) -> {
                        if(white.contains(k)){
                            role.add(v);
                        }
                    });
                }
            }
            return role;
        }).toList()).forEach((role) -> {
            map.put(role.getName(), role);
        });
        return map;
    }

    private void consumers(Map<String, Role> roles){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Consumer administrator = new Consumer();
        administrator.setNickname("administrator");
        administrator.setAvatar("http://localhost:8080/smile.png");
        administrator.setPassword(passwordEncoder.encode("z123."));
        administrator.setUsername("administrator");
        administrator.add(roles.get("administrator"));
        consumerRepository.saveAndFlush(administrator);

        Consumer consumer = new Consumer();
        consumer.setNickname("consumer");
        consumer.setAvatar("http://localhost:8080/smile.png");
        consumer.setPassword(passwordEncoder.encode("z123."));
        consumer.setUsername("consumer");
        consumer.add(roles.get("consumer"));
        consumerRepository.saveAndFlush(consumer);
    }
}
