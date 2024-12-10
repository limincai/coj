package com.mincai.ikuncode.model.dto.questisonsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * @author limincai
 * 判题信息封装类
 */
@Data
public class QuestionJudgeInfo implements Serializable {

    /**
     * 题目执行信息
     */
    private String message;

    /**
     * 题目执行时间：单位 ms
     */
    private Long time;

    /**
     * 题目消耗内存：单位 kb
     */
    private Long memory;

    private static final long serialVersionUID = 1L;
}
