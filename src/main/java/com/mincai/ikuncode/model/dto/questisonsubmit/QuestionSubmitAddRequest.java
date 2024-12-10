package com.mincai.ikuncode.model.dto.questisonsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目提交表
 *
 * @author limincai
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 题目提交关联 questionId
     */
    private Long questionId;

    /**
     * 提交代码语言
     */
    private String language;

    /**
     * 提交代码
     */
    private String code;


    private static final long serialVersionUID = 1L;
}