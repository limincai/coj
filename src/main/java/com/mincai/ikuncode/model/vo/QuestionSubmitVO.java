package com.mincai.ikuncode.model.vo;

import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionJudgeInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author limincai
 */
@Data
public class QuestionSubmitVO implements Serializable {
    /**
     * 题目提交id
     */
    private Long questionSubmitId;

    /**
     * 题目提交关联的 userVO
     */
    private UserVO userVO;

    /**
     * 题目提交关联 questionVO
     */
    private QuestionVO questionVO;

    /**
     * 提交代码语言
     */
    private String language;

    /**
     * 提交代码
     */
    private String code;

    /**
     * 判题信息，json 对象
     */
    private QuestionJudgeInfo questionJudgeInfo;

    /**
     * 判题状态：0-待判题；1-判题中；2-成功；3-失败
     */
    private Integer status;


    /**
     * 创建时间
     */
    private Date createTime;


    private static final long serialVersionUID = 1L;
}