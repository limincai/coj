package com.mincai.ikuncode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.model.domain.QuestionSubmit;
import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionJudgeInfo;
import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionSubmitAddRequest;
import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionSubmitListRequest;
import com.mincai.ikuncode.model.vo.QuestionSubmitVO;

import javax.servlet.http.HttpSession;

/**
 * @author limincai
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     */
    Response<QuestionJudgeInfo> doSubmitQuestion(HttpSession session, QuestionSubmitAddRequest questionSubmitAddRequest);

    /**
     * 题目列表（分页）
     */
    Response<Page<QuestionSubmitVO>> listQuestionSubmitVO(QuestionSubmitListRequest questionSubmitListRequest);


    /**
     * 通过用户 id 和 题目 id 获取题目提交列表（分页）
     */
    Response<Page<QuestionSubmitVO>> listQuestionSubmitVOByUserIdByQuestionId(QuestionSubmitListRequest questionSubmitListRequest);
}
