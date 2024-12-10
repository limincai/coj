package com.mincai.ikuncode.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 题目查询请求类
 *
 * @author limincai
 */
@Data
public class QuestionSearchRequest implements Serializable {
    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 题目标题
     */
    private String questionTitle;

    /**
     * 题目标签 json 字符串
     */
    private List<String> questionTags;

    private static final long serialVersionUID = 1L;
}
