package pers.eloyhere.lively.resolver;

import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class IterableResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Objects.equals(parameter.getParameterType(), Iterable.class);
    }

    @Override
    public @Nullable Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        String name = parameter.getParameterName();
        if (Objects.isNull(name)) {
            return Collections.emptyList();
        }
        String value = webRequest.getParameter(name);
        if (Objects.isNull(value)) {
            return Collections.emptyList();
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(ArrayList.class);
        return reader.readValue(value);
    }
}
