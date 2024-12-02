package com.mincai.ikuncode.service;


import com.mincai.ikuncode.common.Response;

/**
 * @author limincai
 * 邮箱服务
 */
public interface EmailService {

    /**
     * 发送注册验证码
     */
    Response<Void> sendRegisterCaptcha(String redisKey, String userEmail, String subject);


    /**
     * 发送找回密码验证码
     */
    Response<Void> sendRetrievePasswordCaptcha(String redisKey, String userEmail, String subject);
}
