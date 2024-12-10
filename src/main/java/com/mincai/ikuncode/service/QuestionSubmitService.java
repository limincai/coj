package com.mincai.ikuncode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.model.domain.QuestionSubmit;
import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionSubmitAddRequest;

import javax.servlet.http.HttpSession;

/**
 * @author limincai
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     */
    Response<Long> doSubmitQuestion(HttpSession session, QuestionSubmitAddRequest questionSubmitAddRequest);
}
