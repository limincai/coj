package com.mincai.ikuncode.config;

import com.mincai.ikuncode.interceptor.RateLimitInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 拦截器配置
 *
 * @author limincai
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 适配所有路径
        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/**");
    }
}