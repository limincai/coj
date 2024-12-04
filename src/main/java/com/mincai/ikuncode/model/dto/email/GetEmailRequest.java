package com.mincai.ikuncode.model.dto.email;

import lombok.Data;

import java.io.Serializable;

/**
 * 发送邮件请求类
 *
 * @author limincai
 */
@Data
public class GetEmailRequest implements Serializable {

    /**
     * 用户邮箱
     */
    private String userEmail;

    private static final long serialVersionUID = 1L;
}
