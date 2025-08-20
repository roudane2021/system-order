package com.roudane.inventory.infra.transverse.logging;

import com.roudane.transverse.enums.Layer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Aspect
@Component
public class LoggingAspect {


    @Around("execution(* com.roudane.inventory.infra.web.impl..*(..))")
    public Object ControllerMethods(final ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecution(Layer.CONTROLLER, joinPoint);
    }

    @Around("execution(* com.roudane.inventory.infra.messaging.consumer..*(..))")
    public Object ConsumerMethods(final ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecution(Layer.CONSUMER, joinPoint);
    }

    @Around("execution(* com.roudane.inventory.infra.messaging.producer..*(..))")
    public Object ProducerMethods(final ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecution(Layer.PRODUCER, joinPoint);
    }

    @Around("execution(* com.roudane.inventory.infra.persistence..*(..))")
    public Object PersistenceMethods(final ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecution(Layer.PERSISTENCE, joinPoint);
    }

    private  Object  logExecution(final Layer layer, final ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String argsString = buildArgsString(joinPoint.getArgs());
        String className = joinPoint.getSignature().getDeclaringTypeName();

        MDC.put("className", className);
        MDC.put("methodName", methodName);

        try {
            log.info("{} ---> Before:: {}({})", layer, methodName, argsString);
            Object result = joinPoint.proceed();
            log.info("{} ---> AfterReturning:: {}({}) => result = {}", layer , methodName, argsString, result);
            return result;
        } catch (Throwable ex) {
            log.error("{} ---> AfterThrowing:: {}({}) => exception = {}", layer , methodName, argsString, ex.getMessage(), ex);
            throw ex;
        }finally {
            MDC.remove("className");
            MDC.remove("methodName");
        }
    }

    private String buildArgsString(Object[] args) {
        if ((args == null || args.length == 0) ) { return ""; }
        return IntStream.range(0, args.length)
                .mapToObj(i -> String.format(" arg ( %s ) = %s", i +1, args[i]))
                .collect(Collectors.joining(", "));
    }
}
