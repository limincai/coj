package com.mincai.coj.common;

import com.mincai.coj.enums.ErrorCode;

/**
 * 返回工具类
 *
 * @author limincai
 */
public class Result {

    /**
     * 成功
     */
    public static <T> Response<T> success(T data) {
        return new Response<>(0, data, "ok");
    }

    /**
     * 失败
     */
    public static Response<Void> error(ErrorCode errorCode) {
        return new Response<>(errorCode);
    }

    /**
     * 失败
     */
    public static Response<Void> error(int code, String message) {
        return new Response<>(code, null, message);
    }

    /**
     * 失败
     */
    public static Response<Void> error(ErrorCode errorCode, String message) {
        return new Response<>(errorCode.getCode(), null, message);
    }
}
