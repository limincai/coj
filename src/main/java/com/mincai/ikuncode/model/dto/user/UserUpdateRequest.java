package com.mincai.ikuncode.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求类
 *
 * @author limincai
 */
@Data
public class UserUpdateRequest implements Serializable {

    // 用户 id
    private Long userId;

    // 用户昵称
    private String userNickname;

    // 用户密码
    private Long userRole = -1L;

    private static final long serialVersionUID = 1L;
}
