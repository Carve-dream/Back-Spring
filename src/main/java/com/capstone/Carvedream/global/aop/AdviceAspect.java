package com.capstone.Carvedream.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AdviceAspect {
    
    @Around("execution(* com.capstone.Carvedream.advice.*.*(..))")
    public Object adviceController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable, Exception {
        log.error("Adivce Error = {}.{}", proceedingJoinPoint.getSignature().getDeclaringTypeName(), proceedingJoinPoint.getSignature().getName());
        Object result = proceedingJoinPoint.proceed();
        return result;
    }
}
