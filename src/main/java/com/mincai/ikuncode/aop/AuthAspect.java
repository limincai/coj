package com.mincai.ikuncode.aop;

import com.mincai.ikuncode.annotation.CheckLogin;
import com.mincai.ikuncode.constant.UserConstant;
import com.mincai.ikuncode.exception.BusinessException;
import com.mincai.ikuncode.model.enums.ErrorCode;
import com.mincai.ikuncode.model.vo.UserVO;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 用户权限切面类
 *
 * @author limincai
 */
@Component
@Aspect
public class AuthAspect {

    @Resource
    private HttpSession session;

    /**
     * 拦截带有 @AuthCheck 注解的方法
     * 对每个使用该注解的接口进行前置通知
     */
    @Before("@annotation(checkLogin)")
    public void checkAuth(CheckLogin checkLogin) {

        // 从 session 中获取用户信息
        UserVO loginUserVO = (UserVO) session.getAttribute(UserConstant.USER_LOGIN_STATE);

        // 如果用户未登录
        if (loginUserVO == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        // 获取注解定义的角色权限
        int requiredRole = checkLogin.requiredRole();

        // 检查权限是否足够（数值越低权限越大）
        if (requiredRole < loginUserVO.getUserRole()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
    }
}
