package com.mincai.ikuncode.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户修改密码请求类
 *
 * @author limincai
 */
@Data
public class UserRetrievePasswordRequest implements Serializable {

    private static final long serialVersionUID = 1L;

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
}
