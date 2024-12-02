package com.mincai.ikuncode.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author limincai
 */
@Data
public class CaptchaPictureDT0 implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 验证码图片 url
     */
    private String captchaUrl;

    /**
     * 验证码图片 key
     */
    private String captchaKey;
}
