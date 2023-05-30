package ru.clevertec.newsservice.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.newsservice.cache.Cache;
import ru.clevertec.newsservice.cache.factory.CacheFactory;

import java.lang.reflect.Method;

/**
 * The NewsCacheAspect class is an aspect that intercepts method invocations on the NewsServiceImpl and caches
 * the results of the method calls. This aspect uses the {@link CacheFactory} to create and manage a cache.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Profile("dev")
public class NewsCacheAspect {

    private final CacheFactory<Object, Object> cacheFactory;

    /**
     * This method intercepts method invocations on the NewsServiceImpl that have the GetCacheable annotation and
     * caches the results of the method calls. If the result is already cached, it will be retrieved from the cache
     * instead of invoking the method.
     *
     * @param joinPoint the ProceedingJoinPoint.
     * @return the result of the intercepted method invocation.
     * @throws Throwable if an error occurs while invoking the intercepted method.
     */
    @Around("ru.clevertec.newsservice.aop.pointcut.Pointcuts.isMethodWithGetCacheableAnnotation() " +
            "&& ru.clevertec.newsservice.aop.pointcut.Pointcuts.isNewsServiceImplLayer()")
    public Object aroundGetCacheableAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object id = args[0];
        Cache<Object, Object> newsCache = cacheFactory.createNewsCache();
        Object result = newsCache.get(id);
        if (result == null) {
            result = joinPoint.proceed(joinPoint.getArgs());
            newsCache.put(id, result);
            log.info("News Cache put: " + newsCache);
            return result;
        }
        log.info("News Cache get: " + newsCache);
        return result;
    }

    /**
     * This method intercepts method invocations on the NewsServiceImpl that have the PutCacheable annotation and
     * caches the results of the save and update method calls.
     *
     * @param joinPoint the ProceedingJoinPoint.
     * @return the result of the intercepted method invocation.
     * @throws Throwable if an error occurs while invoking the intercepted method.
     */
    @Around("ru.clevertec.newsservice.aop.pointcut.Pointcuts.isMethodWithPutCacheableAnnotation() " +
            "&& ru.clevertec.newsservice.aop.pointcut.Pointcuts.isNewsServiceImplLayer()")
    public Object aroundPutCacheableAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed(joinPoint.getArgs());
        Class<?> aClass = result.getClass();
        Method method = aClass.getMethod("id");
        Object id = method.invoke(result);
        Cache<Object, Object> newsCache = cacheFactory.createNewsCache();
        newsCache.put(id, result);
        log.info("News Cache put: " + newsCache);
        return result;
    }

    /**
     * This method intercepts method invocations on the NewsServiceImpl that have the RemoveCacheable annotation and
     * removes NewsResponse object from the cache.
     *
     * @param joinPoint the ProceedingJoinPoint.
     * @return the result of the intercepted method invocation.
     * @throws Throwable if an error occurs while invoking the intercepted method.
     */
    @Around("ru.clevertec.newsservice.aop.pointcut.Pointcuts.isMethodWithRemoveCacheableAnnotation() " +
            "&& ru.clevertec.newsservice.aop.pointcut.Pointcuts.isNewsServiceImplLayer()")
    public Object aroundRemoveCacheableAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object id = args[0];
        Object result = joinPoint.proceed(joinPoint.getArgs());
        Cache<Object, Object> newsCache = cacheFactory.createNewsCache();
        newsCache.removeByKey(id);
        log.info("News Cache remove: " + newsCache);
        return result;
    }

}
