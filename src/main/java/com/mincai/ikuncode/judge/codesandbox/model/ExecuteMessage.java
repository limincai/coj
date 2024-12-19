package com.mincai.ikuncode.judge.codesandbox.model;

import lombok.Data;

/**
 * @author limincai
 * 进程执行信息
 */
@Data
public class ExecuteMessage {

    /**
     * 程序退出码
     */
    private Integer exitValue;

    /**
     * 正常执行信息
     */
    private String message;

    /**
     * 错误执行信息
     */
    private String errorMessage;

    /**
     * 执行代码用时（ms）
     */
    private Long time;

    /**
     * 执行代码消耗内存（kb）
     */
    private Long memory;
}
