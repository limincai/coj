package com.mincai.ikuncode.model.dto.questisonsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * @author limincai
 */
@Data
public class QuestionSubmitListRequest implements Serializable {
    /**
     * 用户 id
     */
    private Long userId;


    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 当前页码，默认为1
     */
    private Integer currentPage = 1;

    /**
     * 页面尺寸，默认为 10
     */
    private Integer pageSize = 10;

    private static final long serialVersionUID = 1L;
}