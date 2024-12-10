package com.mincai.ikuncode.common;

import com.mincai.ikuncode.model.enums.ErrorCode;

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
     * 成功但不反悔数据
     */
    public static <Void> Response<Void> success() {
        return new Response<Void>(0, "ok");
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
