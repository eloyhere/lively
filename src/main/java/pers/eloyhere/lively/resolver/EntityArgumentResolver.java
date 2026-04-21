package pers.eloyhere.lively.resolver;

import jakarta.persistence.Entity;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.*;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectReader;

import java.lang.reflect.Field;
import java.time.*;

import java.util.*;
import java.time.format.*;
import java.util.function.*;

public class EntityArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String DateFormatPattern = "yyyy-MM-dd HH:mm:ss";

    private final HashMap<Class<?>, Function<String, ?>> handler = new HashMap<>();

    public EntityArgumentResolver(){
        handler.put(UUID.class, (Function<String, UUID>) uuid -> {
            uuid = uuid.replace("\"", "");
            return UUID.fromString(uuid);
        });
        handler.put(Integer.class, (Function<String, Integer>) Integer::parseInt);
        handler.put(LocalDate.class, (Function<String, LocalDate>) text -> LocalDate.parse(text, DateTimeFormatter.ofPattern(DateFormatPattern)));
        handler.put(LocalTime.class, (Function<String, LocalTime>) text -> LocalTime.parse(text, DateTimeFormatter.ofPattern(DateFormatPattern)));
        handler.put(LocalDateTime.class, (Function<String, LocalDateTime>) text -> LocalDateTime.parse(text, DateTimeFormatter.ofPattern(DateFormatPattern)));
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz.isAnnotationPresent(Entity.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if(Objects.nonNull(request) && Objects.nonNull(request.getParameter("payload"))){
            String payload = request.getParameter("payload").replace("\\", "");
            if(payload.startsWith("\"")){
                payload = payload.substring(1);
            }
            if(payload.endsWith("\"")){
                payload = payload.substring(0, payload.length() - 1);
            }
            ObjectMapper mapper = new ObjectMapper();
            ObjectReader reader = mapper.readerFor(HashMap.class);
            Map<String, Object> properties = reader.readValue(payload);
            Class<?> clazz = parameter.getParameterType();
            Object object = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = properties.get(field.getName());
                if(Objects.nonNull(value)){
                    field.set(object, value);
                }
            }
            return object;
        }
        Class<?> clazz = parameter.getParameterType();
        Object object = clazz.getDeclaredConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, String[]> parameterMap = Objects.requireNonNull(request).getParameterMap();
        for (Field field : fields) {
            field.setAccessible(true);
            String[] values = parameterMap.get(field.getName());
            if(Objects.nonNull(values) && values.length > 0){
                field.set(object, handler.getOrDefault(field.getType(), content -> content).apply(values[0]));
            }
        }
        return object;
    }
}
