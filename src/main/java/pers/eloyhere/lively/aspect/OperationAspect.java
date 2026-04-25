package pers.eloyhere.lively.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.entity.log.Operation;
import pers.eloyhere.lively.service.log.OperationService;

import java.util.Objects;


public class OperationAspect implements BaseAspect{

    private final OperationService operationService;

    public OperationAspect(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    @Pointcut("execution(* pers.eloyhere.lively.controller..*.*(..)) || @within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController) && (!execution(* pers.eloyhere.lively.controller.log..*.*(..)))")
    public void pointcut() {

    }

    @Override
    @After("pointcut()")
    public void after(JoinPoint point) {
        BaseAspect.super.after(point);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(Objects.nonNull(attributes)) {
            HttpServletRequest request = attributes.getRequest();
            HttpSession session = request.getSession(false);
            if(Objects.isNull(session.getAttribute("operated"))){
                SecurityContext context = SecurityContextHolder.getContext();
                Authentication authentication = context.getAuthentication();
                if(Objects.nonNull(authentication) && authentication.getPrincipal() instanceof Consumer consumer && !request.getRequestURI().contains("operation")){
                    Operation operation = new Operation();
                    operation.setConsumer(consumer);
                    operation.setUrl(request.getRequestURI());
                    //operationService.insert(operation);
                }
            }
        }
    }
}
