package com.mincai.coj.constant;

/**
 * @author limincai
 * 邮件常量
 */
public interface EmailConstant {

    /**
     * 邮箱注册验证码 redis key
     */
    String USER_REGISTER_CAPTCHA_REDIS_KEY = "user:register:captcha:";

    /**
     * 修改密码邮箱发送主题
     */
    String SEND_MAIL_SUBJECT_REGISTER = "来自 COJ,您的注册验证码";
}
