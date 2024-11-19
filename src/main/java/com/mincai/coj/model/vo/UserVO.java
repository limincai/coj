package com.mincai.coj.model.vo;

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
    private Integer userId;

    /**
     * 用户角色（0 -超级 管理员；1  -  管理员；2 - 普通用户）
     */
    private Integer userRole;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 用户头像地址
     */
    private String userAvatarUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
