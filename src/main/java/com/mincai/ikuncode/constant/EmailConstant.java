package com.mincai.ikuncode.constant;

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
     * 邮箱找回密码验证码 redis key
     */
    String USER_RETRIEVE_PASSWORD_CAPTCHA_REDIS_KEY = "user:retrieve:password:captcha:";


    /**
     * 验证码邮件文件名模板
     */
    String CAPTCHA_EMAIL_TEMPLATE = "captcha-email-template";
}
