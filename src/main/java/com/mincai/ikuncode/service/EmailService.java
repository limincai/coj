package com.mincai.ikuncode.service;


import com.mincai.ikuncode.common.Response;

/**
 * @author limincai
 * 邮箱服务
 */
public interface EmailService {

    /**
     * 发送验证码
     */
    Response<Void> sendCaptcha(String redisKey, String userEmail, String subject);

    /**
     * 删除验证码
     */
    void deleteCaptcha(String redisKey, String userEmail);
}
