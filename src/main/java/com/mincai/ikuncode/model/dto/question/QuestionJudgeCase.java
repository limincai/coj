package com.mincai.ikuncode.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目判题用例封装类
 *
 * @author limincai
 */
@Data
public class QuestionJudgeCase implements Serializable {

    /**
     * 输入用例
     */
    private String input;

    /**
     * 输出用例
     */
    private String output;

    private static final long serialVersionUID = 1L;
}
