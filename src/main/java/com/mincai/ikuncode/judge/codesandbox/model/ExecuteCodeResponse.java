package com.mincai.ikuncode.judge.codesandbox.model;

import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionJudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author limincai
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse implements Serializable {
    /**
     * 输出用例
     */
    private List<String> outputList;

    /**
     * 执行信息
     */
    private String message;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 判题信息
     */
    private QuestionJudgeInfo questionJudgeInfo;

    private static final long serialVersionUID = 1L;
}
