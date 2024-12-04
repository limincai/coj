package com.mincai.ikuncode.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author limincai
 * 用户登录请求类
 */
@Data
public class UserLoginRequest implements Serializable {

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 验证码
     */
    private String captcha;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 图片验证码 key
     */
    private String captchaKey;

    private static final long serialVersionUID = 1L;
}
