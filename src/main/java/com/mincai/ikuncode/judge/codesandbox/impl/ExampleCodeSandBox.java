package com.mincai.ikuncode.judge.codesandbox.impl;

import com.mincai.ikuncode.constant.QuestionJudgeInfoMessage;
import com.mincai.ikuncode.constant.QuestionSubmitStatus;
import com.mincai.ikuncode.judge.codesandbox.CodeSandBox;
import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeRequest;
import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeResponse;
import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionJudgeInfo;

import java.util.List;

/**
 * 实例代码沙箱
 *
 * @author limincai
 */
public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatus.SUCCEED);
        QuestionJudgeInfo questionJudgeInfo = new QuestionJudgeInfo();
        questionJudgeInfo.setMessage(QuestionJudgeInfoMessage.ACCEPTED);
        questionJudgeInfo.setTime(0L);
        questionJudgeInfo.setMemory(0L);
        executeCodeResponse.setQuestionJudgeInfo(questionJudgeInfo);
        System.out.println("示例代码沙箱");

        return executeCodeResponse;
    }
}
