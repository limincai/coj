package com.mincai.coj.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author limincai
 */
@Data
public class UserDTO implements Serializable {

    /**
     * 用户 id
     */

    private Integer userId = -1;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 验证码
     */
    private String captcha;

    /**
     * 用户确认密码
     */
    private String userConfirmedPassword;

    private static final long serialVersionUID = 1L;
}
