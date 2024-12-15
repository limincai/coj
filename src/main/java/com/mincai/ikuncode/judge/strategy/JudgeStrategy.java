package com.mincai.ikuncode.judge.strategy;

import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionJudgeInfo;

/**
 * 判题策略
 *
 * @author limincai
 */
public interface JudgeStrategy {

    /**
     * 判题
     */
    QuestionJudgeInfo doJudge(JudgeContext judgeContext);
}
