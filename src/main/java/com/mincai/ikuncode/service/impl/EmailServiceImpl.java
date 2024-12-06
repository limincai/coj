package com.mincai.ikuncode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.common.Result;
import com.mincai.ikuncode.config.RabbitMQConfig;
import com.mincai.ikuncode.enums.ErrorCode;
import com.mincai.ikuncode.exception.BusinessException;
import com.mincai.ikuncode.mapper.UserMapper;
import com.mincai.ikuncode.model.domain.User;
import com.mincai.ikuncode.mq.EmailMessage;
import com.mincai.ikuncode.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    RabbitTemplate rabbitTemplate;

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
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该邮箱已被注册");
        }

        // 保存验证码到 redis 并发送消息给 mq，通知发送邮箱
        saveCaptchaToRedisAndPublicMessage(redisKey, userEmail, subject);

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

        // 保存验证码到 redis 并发送消息给 mq，通知发送邮箱
        saveCaptchaToRedisAndPublicMessage(redisKey, userEmail, subject);

        return Result.success();
    }

    /**
     * 保存验证码到 redis 并发送消息给 mq，通知发送邮箱
     */
    private void saveCaptchaToRedisAndPublicMessage(String redisKey, String userEmail, String subject) {
        // 生成验证码
        String captcha = generateCaptcha();

        // 发布邮件任务到 RabbitMQ
        EmailMessage emailMessage = new EmailMessage(subject, userEmail, captcha);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE, RabbitMQConfig.EMAIL_ROUTING_KEY, emailMessage);

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
}