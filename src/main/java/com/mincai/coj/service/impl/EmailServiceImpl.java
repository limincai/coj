package com.mincai.coj.service.impl;

import com.mincai.coj.common.Response;
import com.mincai.coj.common.Result;
import com.mincai.coj.constant.EmailConstant;
import com.mincai.coj.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author limincai
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    /**
     * 发送验证码
     */
    @Override
    public Response<Void> sendCaptcha(String redisKey, String userEmail, String subject) {
        // 生成验证码
        String captcha = generateCaptcha();

        // 构造邮箱
        // todo 使用 MimeMailMessage 更加个性化的定制邮箱发送内容
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(userEmail);
        message.setSubject(subject);
        message.setText("您的验证码是：" + captcha + "，有效期为 5 分钟。");

        // 发送邮件
        javaMailSender.send(message);

        // 保存验证码到 redis 中 有效期五分钟
        stringRedisTemplate.opsForValue().set(redisKey + userEmail, captcha, 5, TimeUnit.MINUTES);

        // 返回验证码（应该保存到数据库或缓存中）
        return Result.success();
    }

    /**
     * 删除验证码
     */
    @Override
    public void deleteCaptcha(String redisKey, String userEmail) {
        stringRedisTemplate.opsForValue().getAndDelete(redisKey + userEmail);
    }


    /**
     * 生成随机5位数验证码，包含数字和大小写字母
     */
    public String generateCaptcha() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(chars.length());
            captcha.append(chars.charAt(index));
        }

        return captcha.toString();
    }
}
