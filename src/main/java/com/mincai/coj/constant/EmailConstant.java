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
    String MAIL_SUBJECT_REGISTER = "COJ 邮箱注册验证码：";

    /**
     * 验证码邮件文件名模板
     */
    String CAPTCHA_EMAIL_TEMPLATE = "captcha-email-template";
}
