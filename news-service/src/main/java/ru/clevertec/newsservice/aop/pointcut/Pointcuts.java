package ru.clevertec.newsservice.aop.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("@annotation(ru.clevertec.newsservice.aop.annotation.GetCacheable)")
    public void isMethodWithGetCacheableAnnotation() {
    }

    @Pointcut("@annotation(ru.clevertec.newsservice.aop.annotation.PutCacheable)")
    public void isMethodWithPutCacheableAnnotation() {
    }

    @Pointcut("@annotation(ru.clevertec.newsservice.aop.annotation.RemoveCacheable)")
    public void isMethodWithRemoveCacheableAnnotation() {
    }

    @Pointcut("within(ru.clevertec.newsservice.service.impl.NewsServiceImpl)")
    public void isNewsServiceImplLayer() {
    }

    @Pointcut("within(ru.clevertec.newsservice.service.impl.CommentServiceImpl)")
    public void isCommentServiceImplLayer() {
    }

    @Pointcut("within(@ru.clevertec.newsservice.aop.annotation.Loggable *)")
    public void isClassWithLoggableAnnotation() {
    }

}
