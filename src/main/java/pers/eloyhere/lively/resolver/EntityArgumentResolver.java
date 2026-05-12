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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
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

    protected String resolveJSON(String value){
        if(Objects.isNull(value)){
            return null;
        }
        if(value.startsWith("\"")){
            value = value.substring(1);
            if(value.endsWith("\"")){
                value = value.substring(0, value.length() - 1);
            }
        }
        return value.replace("\\\"", "").trim();
    }

    private boolean availableFromPayload(HttpServletRequest request){
        return Objects.nonNull(request.getParameter("payload"));
    }

    private boolean availableFromJSON(HttpServletRequest request){
        String content = request.getContentType();
        return Objects.nonNull(content) && (content.toLowerCase(Locale.ROOT).contains("application/json") || content.contains("application/json;charset=utf-8"));
    }

    protected Object resolveFromJSON(@NonNull MethodParameter parameter, HttpServletRequest request) throws Exception {
        InputStream inputStream = request.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        CharBuffer buffer = CharBuffer.allocate(inputStream.available());
        inputStreamReader.read(buffer);
        String value = resolveJSON(buffer.toString());
        if(value.contentEquals("{}") || value.isEmpty()){
            Class<?> clazz = parameter.getParameterType();
            return clazz.getDeclaredConstructor().newInstance();
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(parameter.getParameterType());
        return reader.readValue(value);
    }

    protected Object resolveFromPayload(@NonNull MethodParameter parameter, HttpServletRequest request) throws Exception {
        String payload = resolveJSON(request.getParameter("payload"));
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(parameter.getParameterType());
        if(payload.contentEquals("{}") || payload.isEmpty()){
            return reader.readValue("{}");
        }
        return reader.readValue(payload);
    }

    protected Object resolveFromParameters(@NonNull MethodParameter parameter, HttpServletRequest request) throws Exception {
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

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if(Objects.nonNull(request)){
            if(availableFromPayload(request)){
                return resolveFromPayload(parameter, request);
            }
            if(availableFromJSON(request)){
                return resolveFromJSON(parameter, request);
            }
            return resolveFromParameters(parameter, request);
        }
        Constructor<?> constructor = parameter.getParameterType().getDeclaredConstructor();
        return constructor.newInstance();
    }
}
