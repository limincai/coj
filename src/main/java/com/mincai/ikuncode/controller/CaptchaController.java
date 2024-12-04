package com.mincai.ikuncode.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.common.Result;
import com.mincai.ikuncode.constant.CaptchaConstant;
import com.mincai.ikuncode.enums.ErrorCode;
import com.mincai.ikuncode.exception.BusinessException;
import com.mincai.ikuncode.model.dto.captcha.CaptchaPictureDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码接口
 *
 * @author limincai
 */
@RestController
@RequestMapping("/captcha")
@Slf4j
public class CaptchaController {

    @Resource
    private DefaultKaptcha kaptcha;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/get")
    public Response<CaptchaPictureDto> getCaptcha() {
        // 生成唯一标识符（UUID）
        String captchaKey = UUID.randomUUID().toString();

        // 生成验证码文本
        String captchaText = kaptcha.createText();

        // 将验证码存入 redis 中
        stringRedisTemplate.opsForValue().set(CaptchaConstant.USER_LOGIN_CAPTCHA_REDIS_KEY + captchaKey, captchaText, 3, TimeUnit.MINUTES);

        // 创建验证码图片
        BufferedImage captchaImage = kaptcha.createImage(captchaText);

        // 将图片转为 Base64
        String base64Image;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(captchaImage, "jpeg", outputStream);
            base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "验证码生成失败,请重试");
        }

        CaptchaPictureDto captchaPictureDto = new CaptchaPictureDto();
        captchaPictureDto.setCaptchaUrl("data:image/jpeg;base64," + base64Image);
        captchaPictureDto.setCaptchaKey(captchaKey);

        return Result.success(captchaPictureDto);
    }
}
