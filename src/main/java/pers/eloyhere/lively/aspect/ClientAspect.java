package pers.eloyhere.lively.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pers.eloyhere.lively.entity.consumer.Client;
import pers.eloyhere.lively.service.consumer.ClientService;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;


public class ClientAspect  {

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    private final ClientService clientService;

    private final ConcurrentHashMap<String, Long> repeat = new ConcurrentHashMap<>();

    private final Long cooldown = 500L;

    public ClientAspect(HttpServletRequest request, HttpServletResponse response, ClientService clientService) {
        this.request = request;
        this.response = response;
        this.clientService = clientService;
    }

    @Pointcut("""
        @within(org.springframework.stereotype.Controller) ||
        @within(org.springframework.web.bind.annotation.RestController) ||
        @annotation(org.springframework.web.bind.annotation.RequestMapping) ||
        @annotation(org.springframework.web.bind.annotation.GetMapping) ||
        @annotation(org.springframework.web.bind.annotation.PostMapping) ||
        @annotation(org.springframework.web.bind.annotation.PutMapping) ||
        @annotation(org.springframework.web.bind.annotation.DeleteMapping) ||
        @annotation(org.springframework.web.bind.annotation.PatchMapping)
        @annotation(org.springframework.web.bind.annotation.RequestMapping) ||
        @annotation(org.springframework.web.bind.annotation.GetMapping) ||
        @annotation(org.springframework.web.bind.annotation.PostMapping) ||
        @annotation(org.springframework.web.bind.annotation.PutMapping) ||
        @annotation(org.springframework.web.bind.annotation.DeleteMapping) ||
        @annotation(org.springframework.web.bind.annotation.PatchMapping)
    """)
    public void pointcut(){

    }

    @Around("""
        (@within(org.springframework.stereotype.Controller) ||
        @within(org.springframework.web.bind.annotation.RestController) ||
        @annotation(org.springframework.web.bind.annotation.RequestMapping) ||
        @annotation(org.springframework.web.bind.annotation.GetMapping) ||
        @annotation(org.springframework.web.bind.annotation.PostMapping) ||
        @annotation(org.springframework.web.bind.annotation.PutMapping) ||
        @annotation(org.springframework.web.bind.annotation.DeleteMapping) ||
        @annotation(org.springframework.web.bind.annotation.PatchMapping) ||
        @annotation(org.springframework.web.bind.annotation.RequestMapping) ||
        @annotation(org.springframework.web.bind.annotation.GetMapping) ||
        @annotation(org.springframework.web.bind.annotation.PostMapping) ||
        @annotation(org.springframework.web.bind.annotation.PutMapping) ||
        @annotation(org.springframework.web.bind.annotation.DeleteMapping) ||
        @annotation(org.springframework.web.bind.annotation.PatchMapping)) &&
        !execution(* *..monitor.*.*(..))
    """)
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        ArrayList<RequestMatcher> matchers = new ArrayList<>();
        matchers.add(PathPatternRequestMatcher.pathPattern("/"));
        matchers.add(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/*.*"));
        OrRequestMatcher matcher = new OrRequestMatcher(matchers);
        if(matcher.matches(request)) {
            return joinPoint.proceed();
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String fingerprint = request.getRemoteUser()
            .concat(Stream.of("X-Forwarded-For",
                    "Proxy-Client-IP",
                    "WL-Proxy-Client-IP",
                    "HTTP_CLIENT_IP",
                    "HTTP_X_FORWARDED_FOR"
            ).map(request::getHeader).reduce("", String::concat))
            .concat(String.valueOf(request.getHeader("User-Agent")));
        if(repeat.containsKey(fingerprint) && (System.currentTimeMillis() - repeat.get(fingerprint)) <= cooldown){
            throw new OperationNotSupportedException("Too many requests.");
        }
        this.repeat.put(fingerprint, System.currentTimeMillis());
        Client client = new Client();
        client.setFingerprint(fingerprint);
        if(clientService.existBy(client)){
            client.setFingerprint(fingerprint);
            clientService.update(client);
        }else{
            clientService.insert(client);
        }
        return joinPoint.proceed();
    }
}
