package com.mincai.coj.common;

import java.io.Serializable;

import com.mincai.coj.enums.ErrorCode;
import lombok.Data;

/**
 * 通用返回类
 *
 * @author limincai
 */
@Data
public class Response<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public Response(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Response(int code, T data) {
        this(code, data, "");
    }

    public Response(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
