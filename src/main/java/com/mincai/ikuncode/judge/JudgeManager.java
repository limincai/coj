package com.mincai.ikuncode.judge;

import com.mincai.ikuncode.constant.QuestionSubmitLanguage;
import com.mincai.ikuncode.judge.strategy.DefaultJudgeStrategy;
import com.mincai.ikuncode.judge.strategy.JavaJudgeStrategy;
import com.mincai.ikuncode.judge.strategy.JudgeContext;
import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionJudgeInfo;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 *
 * @author limincai
 */
@Service
public class JudgeManager {

    /**
     * 根据语言判断使用哪种判题策略
     */
    public QuestionJudgeInfo doJudge(JudgeContext judgeContext) {
        String language = judgeContext.getQuestionSubmit().getLanguage();
        if (language.equals(QuestionSubmitLanguage.JAVA)) {
            return new JavaJudgeStrategy().doJudge(judgeContext);
        }
        return new DefaultJudgeStrategy().doJudge(judgeContext);
    }
}
