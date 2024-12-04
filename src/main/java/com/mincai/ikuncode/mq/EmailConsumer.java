package com.mincai.ikuncode.mq;

import com.mincai.ikuncode.config.RabbitMQConfig;
import com.mincai.ikuncode.constant.EmailConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;


/**
 * 邮件 mq 消费者
 *
 * @author limincai
 */
@Service
@Slf4j
public class EmailConsumer {

    @Resource
    JavaMailSender javaMailSender;

    @Resource
    TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String username;

    /**
     * 发送验证码邮件给用户
     */
    @RabbitListener(bindings = @QueueBinding(
            // 绑定的队列
            value = @Queue(value = RabbitMQConfig.EMAIL_QUEUE, durable = "true"),
            // 绑定的交换机
            exchange = @Exchange(value = RabbitMQConfig.EMAIL_EXCHANGE),
            // Binging Key
            key = RabbitMQConfig.EMAIL_ROUTING_KEY))
    public void consumeEmailMessage(EmailMessage emailMessage) {

        try {
            // 创建邮件上下文
            Context context = new Context();
            context.setVariable("subject", emailMessage.getSubject());
            context.setVariable("captcha", emailMessage.getCaptcha());
            String htmlContent = templateEngine.process(EmailConstant.CAPTCHA_EMAIL_TEMPLATE, context);

            // 创建 MimeMessage 对象
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 设置邮件相关信息
            helper.setFrom(username, "坤码网");
            helper.setTo(emailMessage.getUserEmail());
            helper.setSubject(emailMessage.getSubject());
            helper.setText(htmlContent, true);

            // 发送邮件
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("邮件发送失败: {}", e.getMessage());
        }
    }
}
