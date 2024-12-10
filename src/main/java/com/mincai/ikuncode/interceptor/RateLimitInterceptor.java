package com.mincai.ikuncode.interceptor;

import com.mincai.ikuncode.exception.BusinessException;
import com.mincai.ikuncode.model.enums.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限制用户请求次数拦截器
 * 3秒内最多请求3次
 *
 * @author limincai
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    // 存储用户请求记录（使用队列来实现滑动时间窗口）
    private final Map<String, LinkedList<Long>> userRequests = new ConcurrentHashMap<>();

    // 时间窗口长度（毫秒）
    private static final long TIME_WINDOW = 3000;


    // 最大请求次数
    private static final int MAX_REQUESTS = 3;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 获取用户唯一标识（如 IP 地址）
        String userKey = request.getRemoteAddr();

        // 当前时间
        long currentTime = System.currentTimeMillis();

        // 获取用户的请求队列
        LinkedList<Long> requestTimestamps = userRequests.computeIfAbsent(userKey, k -> new LinkedList<>());

        synchronized (requestTimestamps) {
            // 移除时间窗口外的请求记录
            while (!requestTimestamps.isEmpty() && currentTime - requestTimestamps.peekFirst() > TIME_WINDOW) {
                requestTimestamps.pollFirst();
            }

            // 检查当前请求次数是否超过限制
            if (requestTimestamps.size() >= MAX_REQUESTS) {
                throw new BusinessException(ErrorCode.TOO_MANY_REQUESTS);
            }

            // 添加当前请求时间到队列
            requestTimestamps.addLast(currentTime);
        }

        return true;
    }
}