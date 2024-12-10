package com.mincai.ikuncode.model.vo;

import com.mincai.ikuncode.model.dto.question.QuestionJudgeCase;
import com.mincai.ikuncode.model.dto.question.QuestionJudgeConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author limincai
 */
@Data
public class QuestionAdminVO implements Serializable {

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 题目标题
     */
    private String questionTitle;

    /**
     * 题目描述
     */
    private String questionDescription;

    /**
     * 题目标签 json 字符串
     */
    private List<String> questionTags;

    /**
     * 题目答案
     */
    private String questionAnswer;

    /**
     * 题目提交数量
     */
    private Long questionSubmitNum;

    /**
     * 题目通过数量
     */
    private Long questionAcceptedNum;

    /**
     * 判题用例 json 对象
     */
    private List<QuestionJudgeCase> questionJudgeCase;

    /**
     * 判题配置 json 对象
     */
    private QuestionJudgeConfig questionJudgeConfig;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
