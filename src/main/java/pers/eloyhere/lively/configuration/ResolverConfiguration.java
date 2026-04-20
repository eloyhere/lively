package pers.eloyhere.lively.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pers.eloyhere.lively.resolver.CollectionResolver;
import pers.eloyhere.lively.resolver.EntityArgumentResolver;
import pers.eloyhere.lively.resolver.IterableResolver;
import pers.eloyhere.lively.resolver.UUIDArgumentResolver;

import java.util.List;

@Configuration
public class ResolverConfiguration implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new IterableResolver());
        resolvers.add(new CollectionResolver());
        resolvers.add(new EntityArgumentResolver());
        resolvers.add(new UUIDArgumentResolver());
    }
}