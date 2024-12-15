package com.mincai.ikuncode.judge;

import cn.hutool.json.JSONUtil;
import com.mincai.ikuncode.constant.CodeSandBoxType;
import com.mincai.ikuncode.constant.QuestionSubmitStatus;
import com.mincai.ikuncode.exception.BusinessException;
import com.mincai.ikuncode.judge.codesandbox.CodeSandBox;
import com.mincai.ikuncode.judge.codesandbox.CodeSandBoxFactory;
import com.mincai.ikuncode.judge.codesandbox.CodeSandBoxProxy;
import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeRequest;
import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeResponse;
import com.mincai.ikuncode.judge.strategy.JudgeContext;
import com.mincai.ikuncode.model.domain.Question;
import com.mincai.ikuncode.model.domain.QuestionSubmit;
import com.mincai.ikuncode.model.dto.question.QuestionJudgeCase;
import com.mincai.ikuncode.model.dto.question.QuestionJudgeConfig;
import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionJudgeInfo;
import com.mincai.ikuncode.model.enums.ErrorCode;
import com.mincai.ikuncode.service.QuestionService;
import com.mincai.ikuncode.service.QuestionSubmitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author limincai
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    QuestionService questionService;

    @Resource
    QuestionSubmitService questionSubmitService;

    @Resource
    JudgeManager judgeManager;

    @Override
    public QuestionJudgeInfo doJudge(Long questionSubmitId) {
        // 参数校验
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }


        Long userId = questionSubmit.getUserId();
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        // 判题信息
        QuestionJudgeInfo questionJudgeInfo = JSONUtil.toBean(questionSubmit.getQuestionJudgeInfo(), QuestionJudgeInfo.class);
        Integer status = questionSubmit.getStatus();

        String questionTitle = question.getQuestionTitle();
        String questionDescription = question.getQuestionDescription();
        String questionTags = question.getQuestionTags();
        String questionAnswer = question.getQuestionAnswer();
        Long questionSubmitNum = question.getQuestionSubmitNum();
        Long questionAcceptedNum = question.getQuestionAcceptedNum();
        // 题目判题用例
        List<QuestionJudgeCase> questionJudgeCaseList = JSONUtil.toList(question.getQuestionJudgeCase(), QuestionJudgeCase.class);
        // 题目判题配置
        QuestionJudgeConfig questionJudgeConfig = JSONUtil.toBean(question.getQuestionJudgeConfig(), QuestionJudgeConfig.class);


        // 如果当前题目已经为判题中，取消执行
        if (!status.equals(QuestionSubmitStatus.WAITING)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判断中");
        }

        // 设置题目提交状态为判题中
        questionSubmit.setStatus(QuestionSubmitStatus.RUNNING);
        questionSubmitService.updateById(questionSubmit);

        // 调用代码沙箱进行判题，获取到执行结果
        CodeSandBox codeSandBox = new CodeSandBoxProxy(CodeSandBoxFactory.newInstance(CodeSandBoxType.SAMPLE));
        // 判题用例输入列表
        List<String> questionInputList = questionJudgeCaseList.stream().map(QuestionJudgeCase::getInput).collect(Collectors.toList());
        // 构建执行请求对象
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder().inputList(questionInputList).code(code).language(language).build();
        // 获取执行代码响应对象
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);

        // 根据执行结果设置题目的判题状态和信息
        // 判题（默认策略）
        // 构造判题上下文
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setQuestionJudgeConfig(questionJudgeConfig);
        judgeContext.setExecuteCodeResponse(executeCodeResponse);
        judgeContext.setQuestionJudgeCaseList(questionJudgeCaseList);
        judgeContext.setQuestionSubmit(questionSubmit);

        // 判题判题策略
        // 获取判题信息
        questionJudgeInfo = judgeManager.doJudge(judgeContext);


        // 修改数据库中的判题状态
        questionSubmit.setStatus(QuestionSubmitStatus.SUCCEED);
        // 修改数据库中的判题结果
        questionSubmit.setQuestionJudgeInfo(JSONUtil.toJsonStr(questionJudgeInfo));
        questionSubmitService.updateById(questionSubmit);
        return questionJudgeInfo;
    }
}
