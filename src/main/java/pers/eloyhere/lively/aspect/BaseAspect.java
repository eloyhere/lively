package pers.eloyhere.lively.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public interface BaseAspect {

    default void before(JoinPoint point){

    }

    default void after(JoinPoint point){

    }

    default void pointcut(){

    }

    default void afterReturning(JoinPoint point, Object result){

    }

    default void afterThrowing(JoinPoint point, Throwable throwable){

    }

    default Object around(ProceedingJoinPoint point) throws Throwable{
        return point.proceed();
    }
}
