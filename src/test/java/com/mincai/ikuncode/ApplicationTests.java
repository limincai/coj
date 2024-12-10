package com.mincai.ikuncode;

import com.mincai.ikuncode.model.domain.QuestionSubmit;
import com.mincai.ikuncode.model.dto.question.QuestionAddRequest;
import com.mincai.ikuncode.model.dto.question.QuestionJudgeCase;
import com.mincai.ikuncode.model.dto.question.QuestionJudgeConfig;
import com.mincai.ikuncode.service.QuestionService;
import com.mincai.ikuncode.service.QuestionSubmitService;
import com.mincai.ikuncode.utils.RegUtil;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.Collections;
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
        QuestionAddRequest questionAddRequest = new QuestionAddRequest();
        questionAddRequest.setQuestionTitle("1");
        questionAddRequest.setQuestionDescription("1");
        questionAddRequest.setQuestionTags(Collections.singletonList("1"));
        questionAddRequest.setQuestionAnswer("1");
        QuestionJudgeCase questionJudgeCase = new QuestionJudgeCase();
        questionJudgeCase.setInput("1");
        questionJudgeCase.setOutput("1");
        questionAddRequest.setQuestionJudgeCase(Collections.singletonList(questionJudgeCase));
        QuestionJudgeConfig questionJudgeConfig = new QuestionJudgeConfig();
        questionJudgeConfig.setTimeLimit(1L);
        questionJudgeConfig.setMemoryLimit(1L);
        questionAddRequest.setQuestionJudgeConfig(questionJudgeConfig);
        questionService.questionAdd(questionAddRequest);
    }

    @Test
    void testQuestionSubmitService() {
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(1L);
        questionSubmit.setQuestionId(1L);
        questionSubmit.setLanguage("1");
        questionSubmit.setCode("1");
        questionSubmit.setQuestionJudgeInfo("1");
        questionSubmitService.save(questionSubmit);
    }
}
