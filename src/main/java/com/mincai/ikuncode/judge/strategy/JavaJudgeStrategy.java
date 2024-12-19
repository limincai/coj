package com.mincai.ikuncode.judge.strategy;

import cn.hutool.core.util.StrUtil;
import com.mincai.ikuncode.constant.QuestionJudgeInfoMessage;
import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeResponse;
import com.mincai.ikuncode.model.dto.question.QuestionJudgeCase;
import com.mincai.ikuncode.model.dto.question.QuestionJudgeConfig;
import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionJudgeInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认判题策略
 *
 * @author limincai
 */
public class JavaJudgeStrategy implements JudgeStrategy {

    @Override
    public QuestionJudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionJudgeInfo questionJudgeInfo = new QuestionJudgeInfo();
        List<QuestionJudgeCase> questionJudgeCaseList = judgeContext.getQuestionJudgeCaseList();
        ExecuteCodeResponse executeCodeResponse = judgeContext.getExecuteCodeResponse();
        QuestionJudgeInfo executeCodeQuestionJudgeInfo = executeCodeResponse.getQuestionJudgeInfo();
        List<String> executeCodeOutputList = executeCodeResponse.getOutputList();
        QuestionJudgeConfig questionJudgeConfig = judgeContext.getQuestionJudgeConfig();

        Long executeCodeTime = executeCodeQuestionJudgeInfo.getTime();
        Long executeCodeMemory = executeCodeQuestionJudgeInfo.getMemory();
        // 设置判题消耗内存
        questionJudgeInfo.setMemory(executeCodeMemory);
        // 设置判题消耗时间
        questionJudgeInfo.setTime(executeCodeTime);

        // 题目输入用例
        List<String> questionInputList = questionJudgeCaseList.stream().map(QuestionJudgeCase::getInput).collect(Collectors.toList());
        // 题目输出用例
        List<String> questionOutputList = questionJudgeCaseList.stream().map(QuestionJudgeCase::getOutput).collect(Collectors.toList());
        // 没有输出用例，不需要比对长度
        if (!StrUtil.isEmpty(questionInputList.get(0))) {
            if (executeCodeOutputList.size() != questionInputList.size()) {
                questionJudgeInfo.setMessage(QuestionJudgeInfoMessage.WRONG_ANSWER);
                return questionJudgeInfo;
            }
        }
        // 依次比对输入和输出用例
        for (int i = 0; i < questionJudgeCaseList.size(); i++) {
            if (!questionOutputList.get(i).equals(processOutput(executeCodeOutputList.get(i).trim()))) {
                questionJudgeInfo.setMessage(QuestionJudgeInfoMessage.WRONG_ANSWER);
                return questionJudgeInfo;
            }
        }

        // 判断题目内存和时间限制
        Long timeLimit = questionJudgeConfig.getTimeLimit();
        Long memoryLimit = questionJudgeConfig.getMemoryLimit();
        // todo 内存溢出
        if (timeLimit > 0 && executeCodeMemory > questionJudgeConfig.getMemoryLimit()) {
            questionJudgeInfo.setMessage(QuestionJudgeInfoMessage.MEMORY_LIMIT_EXCEEDED);
            return questionJudgeInfo;
        }
        // todo 超时
        if (memoryLimit > 0 && executeCodeTime > questionJudgeConfig.getTimeLimit()) {
            questionJudgeInfo.setMessage(QuestionJudgeInfoMessage.TIME_LIMIT_EXCEEDED);
            return questionJudgeInfo;
        }
        // 判题成功
        questionJudgeInfo.setMessage(QuestionJudgeInfoMessage.ACCEPTED);
        // 返回判题信息
        return questionJudgeInfo;
    }

    /**
     * 处理用户输出，去掉引号和换行符，处理转义字符
     *
     * @param userOutput 用户输出的字符串
     * @return 处理后的字符串
     */
    public static String processOutput(String userOutput) {
        // 去掉两边的引号（如果有）
        userOutput = userOutput.replaceAll("^\"|\"$", "");

        // 去掉字符串中的换行符（如果有）
        userOutput = userOutput.replaceAll("\\\\n", "");

        // 去除首尾的空格
        userOutput = userOutput.trim();

        // 返回处理后的字符串
        return userOutput;
    }
}
