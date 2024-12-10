package com.mincai.ikuncode.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除题目请求类
 *
 * @author limincai
 */
@Data
public class QuestionDeleteRequest implements Serializable {

    /**
     * 题目id
     */
    private Long questionId;


    private static final long serialVersionUID = 1L;
}
