/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);


    @Before("targetMethods()")
    public void beforeMethodExecution(JoinPoint joinPoint) {
        final String controllerName = joinPoint.getTarget().getClass().getSimpleName();
        final String methodName = joinPoint.getSignature().getName();
        final Object[] args = joinPoint.getArgs();

        final List<String> argList = Arrays.stream(args)
                .map(arg -> arg.getClass().getSimpleName())
                .toList();

        logger.info("Method {}#{} is being executed with arguments: {}", controllerName, methodName, argList);
    }

    @AfterReturning(pointcut = "targetMethods()")
    public void afterMethodExecution(JoinPoint joinPoint) {
        final String controllerName = joinPoint.getTarget().getClass().getSimpleName();
        final String methodName = joinPoint.getSignature().getName();
        logger.info("Method {}#{} was executed successfully", controllerName, methodName);
    }

    @AfterThrowing(pointcut = "targetMethods()", throwing = "exception")
    public void afterMethodThrowing(JoinPoint joinPoint, Throwable exception) {
        final String controllerName = joinPoint.getTarget().getClass().getSimpleName();
        final String methodName = joinPoint.getSignature().getName();
        logger.error("Method {}#{} threw an exception: {}", controllerName, methodName, exception.getMessage());
    }

    @Around("targetMethods()")
    public Object calculateExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        final long startTime = System.currentTimeMillis();

        // Proceed with the method execution
        final Object result = joinPoint.proceed();

        final long endTime = System.currentTimeMillis();

        final String controllerName = joinPoint.getTarget().getClass().getSimpleName();
        final String methodName = joinPoint.getSignature().getName();

        logger.info("Method {}#{} was executed in {} milliseconds and used {} mbs", controllerName, methodName,
                endTime - startTime, getMemoryUsageInMbs());

        return result;
    }


    @Pointcut("execution(public * com.areg.project.controllers.*Controller.*(..)) || " +
            "execution(public * com.areg.project.services.implementations.*Service.*(..))")
    private void targetMethods() {}

    private long getMemoryUsageInMbs() {
        final Runtime runtime = Runtime.getRuntime();
        final long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        //  Convert bytes to megabytes
        return usedMemory / (1024 * 1024);
    }
}
