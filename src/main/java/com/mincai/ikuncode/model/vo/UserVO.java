package com.mincai.ikuncode.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author limincai
 */
@Data
public class UserVO implements Serializable {
    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 用户角色（0 -超级 管理员；1  -  管理员；2 - 普通用户）
     */
    private Integer userRole;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户头像地址
     */
    private String userAvatarUrl;

    /**
     * 用户鸡脚数量
     */
    private Integer userJijiao;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

}
