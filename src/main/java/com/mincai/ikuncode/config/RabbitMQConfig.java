package com.mincai.ikuncode.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rabbit MQ 配置类
 *
 * @author limincai
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {


    // email 交换机
    public static final String EMAIL_EXCHANGE = "email.exchange";

    // email 队列
    public static final String EMAIL_QUEUE = "email.queue";

    // email routingKey
    public static final String EMAIL_ROUTING_KEY = "email.routingKey";

    // rabbit mq 序列化
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
