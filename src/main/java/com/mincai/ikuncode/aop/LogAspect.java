package com.mincai.ikuncode.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


/**
 * @author limincai
 * 打印接口日志信息切面类
 */
@Component
@Aspect
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Resource
    private HttpServletRequest request;

    @Around("execution(* com.mincai.ikuncode.controller..*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取请求信息
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String params = Arrays.toString(joinPoint.getArgs());
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        long startTime = System.currentTimeMillis();

        // 打印请求日志
        logger.info("==== 请求开始 ====");
        logger.info("请求地址: {}", url);
        logger.info("请求方法: {}", method);
        logger.info("请求接口: {}.{}", className, methodName);
        logger.info("参数: {}", params);

        // 执行目标方法
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            logger.error("异常: ", e);
            throw e;
        }

        long endTime = System.currentTimeMillis();

        // 打印响应日志
        logger.info("==== 请求结束 ====");
        logger.info("响应: {}", result);
        logger.info("耗时: {}ms", (endTime - startTime));

        return result;
    }
}
