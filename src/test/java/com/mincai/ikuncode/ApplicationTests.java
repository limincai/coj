package com.mincai.ikuncode;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.UUID;

@SpringBootTest
class ApplicationTests {


    @Resource
    RabbitTemplate rabbitTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void testRabbitMQ() {
        rabbitTemplate.convertAndSend("test", "test");
    }

    @Test
    public void deleteSession() {
        // 构造 Redis 中的 Session Key
        String sessionKey = "spring:session:sessions:5724e33e-1af9-416b-9dbe-94fb420e7c64";

        // 删除对应的 Key
        Boolean result = stringRedisTemplate.delete(sessionKey);

        if (Boolean.TRUE.equals(result)) {
            System.out.println("Session 删除成功: " + sessionKey);
        } else {
            System.out.println("Session 删除失败，Key 不存在: " + sessionKey);
        }
    }

    @Test
    public void testUUID() {
        System.out.println(UUID.randomUUID());
    }

    @Test
    public void test() {
        String s = stringRedisTemplate.opsForValue().get("user:register:verify:code:3206820023@qq.com");
        System.out.println(s);
    }
}
