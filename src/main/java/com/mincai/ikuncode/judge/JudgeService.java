package com.mincai.ikuncode.judge;

import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionJudgeInfo;

/**
 * 判题服务
 *
 * @author limincai
 */
public interface JudgeService {

    /**
     * 判题
     */
    QuestionJudgeInfo doJudge(Long questionSubmitId);
}
