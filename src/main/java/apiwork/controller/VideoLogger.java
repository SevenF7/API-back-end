package apiwork.controller;

import apiwork.utils.RequestMonitor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Aspect
@Component
public class VideoLogger {

    private static final Logger logger = LoggerFactory.getLogger(VideoLogger.class);

    @Autowired
    private RequestMonitor requestMonitor;

    private final AtomicLong requestIdGenerator = new AtomicLong();
    private final ThreadLocal<Long> requestIdHolder = new ThreadLocal<>();

    // 定义切入点，拦截所有controller包下的所有类的所有public方法
    @Pointcut("execution(public * apiwork.controller.*.*(..))")
    public void controllerMethods() {}

    // 前置通知：记录方法开始执行的时间戳
    @Before("controllerMethods()")
    public void logMethodStart(JoinPoint joinPoint) {
        long requestId = requestIdGenerator.incrementAndGet();
        requestIdHolder.set(requestId);
        requestMonitor.logRequestStart(requestId);
        logger.info("Method {}() called with arguments {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    // 后置通知：记录方法执行完成的时间和耗时
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logMethodExecutionTime(JoinPoint joinPoint, Object result) {
        Long requestId = requestIdHolder.get();
        if (requestId != null) {
            long duration = requestMonitor.logRequestEnd(requestId);
            logger.info("Method {}() executed in {} ms", joinPoint.getSignature().getName(), duration);
            requestIdHolder.remove(); // 清理 ThreadLocal
        }
    }

    // 记录错误
    public void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
