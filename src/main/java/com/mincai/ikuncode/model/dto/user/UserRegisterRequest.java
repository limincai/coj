package com.mincai.ikuncode.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求类
 *
 * @author limincai
 */
@Data
public class UserRegisterRequest implements Serializable {

    /**
     * 用户账号
     */
    private String userAccount;


    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 验证码
     */
    private String captcha;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户确认密码
     */
    private String userConfirmedPassword;

    private static final long serialVersionUID = 1L;
}
