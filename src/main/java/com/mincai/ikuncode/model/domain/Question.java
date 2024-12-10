package com.mincai.ikuncode.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目表
 *
 * @author limincai
 */
@TableName(value = "question")
@Data
public class Question implements Serializable {
    /**
     * 题目id
     */
    @TableId(value = "question_id", type = IdType.AUTO)
    private Long questionId;

    /**
     * 题目标题
     */
    @TableField(value = "question_title")
    private String questionTitle;

    /**
     * 题目描述
     */
    @TableField(value = "question_description")
    private String questionDescription;

    /**
     * 题目标签 json 字符串
     */
    @TableField(value = "question_tags")
    private String questionTags;

    /**
     * 题目答案
     */
    @TableField(value = "question_answer")
    private String questionAnswer;

    /**
     * 题目提交数量
     */
    @TableField(value = "question_submit_num")
    private Long questionSubmitNum;

    /**
     * 题目通过数量
     */
    @TableField(value = "question_accepted_num")
    private Long questionAcceptedNum;

    /**
     * 判题用例 json 对象
     */
    @TableField(value = "question_judge_case")
    private String questionJudgeCase;

    /**
     * 判题配置 json 对象
     */
    @TableField(value = "question_judge_config")
    private String questionJudgeConfig;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除，逻辑删除 0-未删除；1-删除
     */
    @TableField(value = "is_deleted")
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}