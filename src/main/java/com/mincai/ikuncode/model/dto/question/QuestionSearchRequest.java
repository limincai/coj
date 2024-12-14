package com.mincai.ikuncode.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目查询请求类
 *
 * @author limincai
 */
@Data
public class QuestionSearchRequest implements Serializable {

    /**
     * 搜索关键字
     */
    private String searchKeyword;

    /**
     * 当前页码（默认是1）
     */
    private Integer currentPage = 1;

    /**
     * 页面尺寸（默认是10）
     */
    private Integer pageSize = 10;

    private static final long serialVersionUID = 1L;
}
