package com.mincai.ikuncode.constant;

/**
 * 判题执行信息常量
 *
 * @author limincai
 */
public interface QuestionJudgeInfoMessage {

    String ACCEPTED = "成功";

    String WRONG_ANSWER = "答案错误";

    String COMPILE_WRONG = "编译错误";

    String MEMORY_LIMIT_EXCEEDED = "内存溢出";

    String TIME_LIMIT_EXCEEDED = "时间溢出";

    String OUTPUT_LIMIT_EXCEEDED = "输出溢出";

    String RUNTIME_ERROR = "运行错误";

    String SYSTEM_ERROR = "系统错误";

    String WAITING = "等待中";
}
