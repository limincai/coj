package com.mincai.ikuncode.judge.codesandbox.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 代码沙箱运行结果的一个状态
 *
 * @author limincai
 */
public enum QuestionExecuteResultEnum {

    CompileFailure("编译失败", 0),

    RunException("运行异常", 1),

    ExecuteSuccess("执行成功", 2),

    OutOfTimeException("超时异常", 3);

    private final String text;

    private final Integer value;

    QuestionExecuteResultEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static QuestionExecuteResultEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionExecuteResultEnum anEnum : QuestionExecuteResultEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

}
