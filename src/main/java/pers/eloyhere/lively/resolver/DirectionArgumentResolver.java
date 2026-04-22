package pers.eloyhere.lively.resolver;

import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class DirectionArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Sort.Direction.class);
    }

    @Override
    public @Nullable Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        String name = parameter.getParameterName();
        if(Objects.isNull(name)){
            return Sort.Direction.ASC;
        }
        TreeSet<String> white = new TreeSet<>();
        white.add("DESC");
        white.add("ASC");
        String value = webRequest.getParameter(name);
        if(Objects.isNull(value)){
            return Sort.Direction.ASC;
        }
        if(white.contains(value)){
            if(value.contentEquals("DESC")){
                return Sort.Direction.DESC;
            }
            return Sort.Direction.ASC;
        }
        return Sort.Direction.ASC;
    }

}
