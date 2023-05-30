package ru.clevertec.loggingstarter.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * The LoggingAspect class is an aspect that intercepts method invocations on controllers and handlers. It's logging
 * the results of the method calls.
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class LoggingAspect {

    @Pointcut("within(@ru.clevertec.loggingstarter.annotation.Loggable *)")
    public void isClassWithLoggableAnnotation() {
    }


    /**
     * This method intercepts method invocations on controllers and handlers that have the Loggable annotation and
     * logging the results of the method calls.
     *
     * @param joinPoint the ProceedingJoinPoint.
     * @return the result of the intercepted method invocation.
     * @throws Throwable if an error occurs while invoking the intercepted method.
     */
    @Around("isClassWithLoggableAnnotation()")
    public Object loggingMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime before = LocalDateTime.now();
        Object result = joinPoint.proceed(joinPoint.getArgs());
        LocalDateTime after = LocalDateTime.now();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String logMessage = """
                %s%s.%s :
                Request : %s
                Response : %s
                Method duration in millis : %s""".formatted("\n",
                className,
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()),
                result,
                Duration.between(before, after).toMillis());
        if (className.endsWith("Handler")) {
            log.error(logMessage);
        } else {
            log.info(logMessage);
        }
        return result;
    }

}
