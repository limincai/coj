package com.mincai.ikuncode;

import com.mincai.ikuncode.model.domain.Question;
import com.mincai.ikuncode.model.domain.QuestionSubmit;
import com.mincai.ikuncode.service.QuestionService;
import com.mincai.ikuncode.service.QuestionSubmitService;
import com.mincai.ikuncode.utils.RegUtil;
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

    @Resource
    QuestionService questionService;

    @Resource
    QuestionSubmitService questionSubmitService;

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

    @Test
    void testReg() {
        if (!RegUtil.isLegalUserEmail("3206820023@qq.com")) {
            System.out.println("邮箱格式错误");
        } else {
            System.out.println("验证码发送成功");
        }
    }

    @Test
    void testQuestionService() {
        Question question = new Question();
        question.setQuestionTitle("1");
        question.setQuestionContent("1");
        question.setQuestionTags("{[1]}");
        question.setQuestionAnswer("1");
        question.setQuestionSubmitNum(1L);
        question.setQuestionAcceptedNum(1L);
        question.setJudgeCase("1");
        question.setJudgeConfig("1");
        questionService.save(question);
    }

    @Test
    void testQuestionSubmitService() {
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(1L);
        questionSubmit.setQuestionId(1L);
        questionSubmit.setLanguage("1");
        questionSubmit.setCode("1");
        questionSubmit.setJudgeInfo("1");
        questionSubmitService.save(questionSubmit);
    }
}
