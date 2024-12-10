package com.mincai.ikuncode.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author limincai
 * 用户表
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
    /**
     * 用户 id
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;

    /**
     * 用户角色（0 -超级 管理员；1  -  管理员；2 - 普通用户）
     */
    @TableField(value = "user_role")
    private Integer userRole;

    /**
     * 用户账号
     */
    @TableField(value = "user_account")
    private String userAccount;

    /**
     * 用户密码
     */
    @TableField(value = "user_password")
    private String userPassword;

    /**
     * 用户邮箱
     */
    @TableField(value = "user_email")
    private String userEmail;

    /**
     * 用户昵称
     */
    @TableField(value = "user_nickname")
    private String userNickname;

    /**
     * 用户简介
     */
    @TableField(value = "user_profile")
    private String userProfile;

    /**
     * 用户头像地址
     */
    @TableField(value = "user_avatar_url")
    private String userAvatarUrl;

    /**
     * 用户鸡脚数量
     */
    @TableField(value = "user_jijiao")
    private Integer userJijiao;

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
     * 是否删除（0-否；1- 删除）
     */
    @TableField(value = "is_deleted")
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}