package com.mincai.ikuncode.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author limincai
 * 邮箱信息封装类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage implements Serializable {

    // 邮件主题
    private String subject;

    // 用户邮箱
    private String userEmail;

    // 验证码
    private String captcha;
}
