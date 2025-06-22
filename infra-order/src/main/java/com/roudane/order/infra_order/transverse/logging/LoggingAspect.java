package com.roudane.order.infra_order.transverse.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.roudane.order.infra_order.adapter..*(..))")
    public void logBeforeMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();

        // Construire la chaîne des arguments en utilisant un Stream
        String argsString = IntStream.range(0, methodArgs.length)
                .mapToObj(i -> "arg" + (i + 1) + "=" + methodArgs[i])
                .collect(Collectors.joining(", "));

        // Afficher le nom de la méthode et les arguments dans la console
        System.out.println(String.format("Before:: %s(%s)", methodName, argsString));
    }

    @AfterReturning(pointcut = "execution(* com.roudane.order.infra_order.adapter..*(..))", returning = "result")
    public void logAfterMethodExecution(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();

        // Construire la chaîne des arguments en utilisant un Stream
        String argsString = IntStream.range(0, methodArgs.length)
                .mapToObj(i -> "arg" + (i + 1) + "=" + methodArgs[i])
                .collect(Collectors.joining(", "));

        // Afficher le nom de la méthode et les arguments dans la console
        System.out.println(String.format("Exception in method: %s(%s) : result -> %s", methodName, argsString, result));
    }

    @AfterThrowing(pointcut = "execution(* com.roudane.order.infra_order.adapter..*(..))", throwing = "exception")
    public void logAfterThrowingMethodExecution(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();

        // Construire la chaîne des arguments en utilisant un Stream
        String argsString = IntStream.range(0, methodArgs.length)
                .mapToObj(i -> "arg" + (i + 1) + "=" + methodArgs[i])
                .collect(Collectors.joining(", "));

        // Afficher le nom de la méthode, les arguments et le message de l'exception
        System.err.println(String.format("Exception in method: %s(%s)", methodName, argsString));
        System.err.println("Exception message: " + exception.getMessage());
    }
}
