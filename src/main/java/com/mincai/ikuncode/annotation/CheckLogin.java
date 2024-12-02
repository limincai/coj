package com.mincai.ikuncode.annotation;

import cn.hutool.core.annotation.AliasFor;
import com.mincai.ikuncode.constant.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author limincai
 * 检查访问的接口用户是否已经登陆，以及登陆的用户需要何种权限
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLogin {

    @AliasFor(attribute = "requiredRole") int value() default UserRole.USER;

    // 默认所需权限为用户
    @AliasFor(attribute = "value") int requiredRole() default UserRole.USER;
}
