package com.mincai.ikuncode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.common.Result;
import com.mincai.ikuncode.constant.EmailConstant;
import com.mincai.ikuncode.enums.ErrorCode;
import com.mincai.ikuncode.exception.BusinessException;
import com.mincai.ikuncode.mapper.UserMapper;
import com.mincai.ikuncode.model.domain.User;
import com.mincai.ikuncode.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author limincai
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Resource
    UserMapper userMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    JavaMailSender javaMailSender;

    @Resource
    TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String username;

    /**
     * 发送注册验证码
     */
    @Override
    public Response<Void> sendRegisterCaptcha(String redisKey, String userEmail, String subject) {
        // 邮箱已经被注册
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserEmail, userEmail);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名已存在");
        }

        // 保存验证码到 redis 并 发送邮件
        saveCaptchaToRedisAndSendEmail(redisKey, userEmail, subject);

        return Result.success();
    }


    /**
     * 发送找回密码验证码
     */
    @Override
    public Response<Void> sendRetrievePasswordCaptcha(String redisKey, String userEmail, String subject) {
        // 邮箱还未被注册
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserEmail, userEmail);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "当前邮箱用户还未注册");
        }

        // 保存验证码到 redis 并 发送邮件
        saveCaptchaToRedisAndSendEmail(redisKey, userEmail, subject);

        return Result.success();
    }

    /**
     * 保存验证码到 redis 并 发送邮件
     */
    private void saveCaptchaToRedisAndSendEmail(String redisKey, String userEmail, String subject) {
        // 生成验证码
        String captcha = generateCaptcha();

        // 发送邮件
        try {
            sendCaptchaEmail(subject, userEmail, captcha);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "邮件发送失败，请重试");
        }

        // 保存验证码到 redis 中 有效期五分钟
        stringRedisTemplate.opsForValue().set(redisKey + userEmail, captcha, 5, TimeUnit.MINUTES);
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

    /**
     * 发送邮件
     */
    private void sendCaptchaEmail(String subject, String toEmail, String captcha) throws MessagingException, UnsupportedEncodingException {
        // 创建邮件上下文
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("captcha", captcha);
        String htmlContent = templateEngine.process(EmailConstant.CAPTCHA_EMAIL_TEMPLATE, context);

        // 创建 MimeMessage 对象
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // 设置邮件相关信息
        helper.setFrom(username, "坤码网");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        // 支持 HTML
        helper.setText(htmlContent, true);

        // 发送邮件
        javaMailSender.send(message);
    }
}
