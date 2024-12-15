package com.mincai.ikuncode.judge.strategy;

import com.mincai.ikuncode.judge.codesandbox.model.ExecuteCodeResponse;
import com.mincai.ikuncode.model.domain.QuestionSubmit;
import com.mincai.ikuncode.model.dto.question.QuestionJudgeCase;
import com.mincai.ikuncode.model.dto.question.QuestionJudgeConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 判题上下文
 *
 * @author limincai
 */
@Data
public class JudgeContext implements Serializable {

    /**
     * 判题配置
     */
    QuestionJudgeConfig questionJudgeConfig;

    /**
     * 代码执行响应
     */
    ExecuteCodeResponse executeCodeResponse;

    /**
     * 题目设置的判题输入输出用例
     */
    List<QuestionJudgeCase> questionJudgeCaseList;

    /**
     * 题目提交信息
     */
    QuestionSubmit questionSubmit;


    private static final long serialVersionUID = 1L;
}
