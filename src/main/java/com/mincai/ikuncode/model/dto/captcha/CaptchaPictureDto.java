package com.mincai.ikuncode.model.dto.captcha;

import lombok.Data;

import java.io.Serializable;

/**
 * 验证码图片数据传输对象
 *
 * @author limincai
 */
@Data
public class CaptchaPictureDto implements Serializable {
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
