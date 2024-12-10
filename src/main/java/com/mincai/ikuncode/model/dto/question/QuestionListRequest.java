package com.mincai.ikuncode.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * @author limincai
 */
@Data
public class QuestionListRequest implements Serializable {

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
