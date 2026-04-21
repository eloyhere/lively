package pers.eloyhere.lively.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class UUIDArgumentResolver implements HandlerMethodArgumentResolver {

    private final TreeSet<String> filter = new TreeSet<>();

    public UUIDArgumentResolver(){
        filter.add("");
        filter.add("null");
        filter.add("undefined");
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Objects.equals(parameter.getParameterType(), UUID.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String uuid = webRequest.getParameter(Objects.requireNonNull(parameter.getParameterName()));
        if(Objects.nonNull(uuid) && !filter.contains(uuid.trim())){
            return UUID.fromString(uuid);
        }
        return null;
    }
}