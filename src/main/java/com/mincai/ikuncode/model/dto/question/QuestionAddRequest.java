package com.mincai.ikuncode.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 添加题目请求类
 *
 * @author limincai
 */
@Data
public class QuestionAddRequest implements Serializable {

    /**
     * 题目标题
     */
    private String questionTitle;

    /**
     * 题目描述
     */
    private String questionDescription;

    /**
     * 题目标签
     */
    private List<String> questionTags;

    /**
     * 题目答案
     */
    private String questionAnswer;

    /**
     * 判题用例 json 对象
     */
    private List<QuestionJudgeCase> questionJudgeCase;

    /**
     * 判题配置 json 对象
     */
    private QuestionJudgeConfig questionJudgeConfig;


    private static final long serialVersionUID = 1L;
}
