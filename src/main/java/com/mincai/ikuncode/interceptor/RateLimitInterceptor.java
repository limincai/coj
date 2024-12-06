package com.mincai.ikuncode.interceptor;

import com.mincai.ikuncode.enums.ErrorCode;
import com.mincai.ikuncode.exception.BusinessException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限制用户请求次数拦截器
 *
 * @author limincai
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    // 存储用户请求时间
    private final Map<String, Long> requestTimestamps = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 获取用户唯一标识 ip
        String userKey = request.getRemoteAddr();

        long currentTime = System.currentTimeMillis();
        Long lastRequestTime = requestTimestamps.get(userKey);

        if (lastRequestTime != null && (currentTime - lastRequestTime < 1000)) {
            // 如果时间间隔小于 1 秒，跑出异常
            throw new BusinessException(ErrorCode.TOO_MANY_REQUESTS);
        }

        // 记录当前请求时间
        requestTimestamps.put(userKey, currentTime);
        return true;
    }
}