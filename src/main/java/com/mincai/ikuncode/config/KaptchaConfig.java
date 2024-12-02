package com.mincai.ikuncode.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


/**
 * 验证码生成工具配置类
 *
 * @author limincai
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        // 验证码图片是否生成边框
        properties.setProperty("kaptcha.border", "no");
        // 验证码图片的字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        // 验证码图片字体的大小
        properties.setProperty("kaptcha.textproducer.font.size", "40");
        // 验证码图片的宽度
        properties.setProperty("kaptcha.image.width", "120");
        // 验证码图片的高度
        properties.setProperty("kaptcha.image.height", "50");
        // 验证码字符长度
        properties.setProperty("kaptcha.textproducer.char.length", "5");
        // 验证码字体
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Courier");

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}