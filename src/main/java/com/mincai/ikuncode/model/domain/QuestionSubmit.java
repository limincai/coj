package com.mincai.ikuncode.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交表
 *
 * @author limincai
 */
@TableName(value = "question_submit")
@Data
public class QuestionSubmit implements Serializable {
    /**
     * 题目提交id
     */
    @TableId(value = "question_submit_id", type = IdType.ASSIGN_ID)
    private Long questionSubmitId;

    /**
     * 题目提交关联的userId
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 题目提交关联 questionId
     */
    @TableField(value = "question_id")
    private Long questionId;

    /**
     * 提交代码语言
     */
    @TableField(value = "language")
    private String language;

    /**
     * 提交代码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 判题信息，json 对象
     */
    @TableField(value = "judge_info")
    private String judgeInfo;

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
     * 是否删除，逻辑删除,0-未删除，1-已删除
     */
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}