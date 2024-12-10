package com.mincai.ikuncode.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目判题配置封装类
 *
 * @author limincai
 */
@Data
public class QuestionJudgeConfig implements Serializable {

    /**
     * 时间限制：单位 ms
     */
    private Long timeLimit;

    /**
     * 内存限制：单位 kb
     */
    private Long memoryLimit;


    private static final long serialVersionUID = 1L;
}
