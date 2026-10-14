package com.tts.car_insurance.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.tts.car_insurance.controller.*.*(..))")
    public void controllerPackagePointcut(){}

    @Before("controllerPackagePointcut()")
    public void logBefore(JoinPoint joinPoint){
        if (logger.isTraceEnabled()) {
            logger.trace("TRACE: Entering method: {} with arguments: {}",
                    joinPoint.getSignature().toShortString(),
                    Arrays.toString(joinPoint.getArgs())
            );
        }
    }

    @Before("controllerPackagePointcut()")
    public void logDebugMethod(JoinPoint joinPoint){
        logger.debug("DEBUG: Executing controller method -> {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "controllerPackagePointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result){
        logger.info("INFO: Method Completed: {} with result: {}",
                joinPoint.getSignature().toShortString(),
                result);
    }


    @AfterThrowing(
            pointcut = "execution(* com.tts.car_insurance.controller.*.*(..))",
            throwing = "exception"
    )
    public void logAccessDeniedOrError(JoinPoint joinPoint, Throwable exception) {
        if (exception instanceof org.springframework.security.access.AccessDeniedException) {
            logger.error("SECURITY [ERROR]: Access Denied (403 Forbidden) caught in method: {}. Reason: {}",
                    joinPoint.getSignature().toShortString(),
                    exception.getMessage());
        } else {
            logger.error("ERROR: Exception in method: {} with cause = {}",
                    joinPoint.getSignature().toShortString(),
                    exception.getMessage());
        }
    }
}