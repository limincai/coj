package com.mincai.ikuncode.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author limincai
 * 用户删除请类
 */
@Data
public class UserDeleteRequest implements Serializable {

    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}