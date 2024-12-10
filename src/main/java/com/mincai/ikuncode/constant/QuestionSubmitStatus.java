package com.mincai.ikuncode.constant;

/**
 * 题目提交状态常量
 *
 * @author limincai
 */
public interface QuestionSubmitStatus {

    /**
     * 等待中
     */
    Integer WAITING = 0;

    /**
     * 运行中
     */
    Integer RUNNING = 1;

    /**
     * 成功
     */
    Integer SUCCEED = 2;

    /**
     * 失败
     */
    Integer FAILED = 3;
}
