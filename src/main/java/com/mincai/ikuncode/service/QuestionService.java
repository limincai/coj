package com.mincai.ikuncode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.model.domain.Question;
import com.mincai.ikuncode.model.dto.question.QuestionAddRequest;
import com.mincai.ikuncode.model.dto.question.QuestionListRequest;
import com.mincai.ikuncode.model.dto.question.QuestionSearchRequest;
import com.mincai.ikuncode.model.dto.question.QuestionUpdateRequest;
import com.mincai.ikuncode.model.vo.QuestionAdminVO;
import com.mincai.ikuncode.model.vo.QuestionVO;

/**
 * @author limincai
 */
public interface QuestionService extends IService<Question> {

    /**
     * 添加题目
     */
    Response<Long> questionAdd(QuestionAddRequest questionAddRequest);

    /**
     * 删除题目
     */
    Response<Void> questionDelete(Long deleteQuestionId);

    /**
     * 题目列表（分页）管理员
     */
    Response<Page<QuestionAdminVO>> questionListByAdmin(QuestionListRequest questionListRequest);


    /**
     * 题目列表（分页）普通用户
     */
    Response<Page<QuestionVO>> questionListByUser(QuestionListRequest questionListRequest);

    /**
     * 根据 id 获取题目 VO
     */
    Response<QuestionVO> questionGetById(Long questionId);

    /**
     * 根据 id 获取题目 AdminVO
     */
    Response<QuestionAdminVO> questionGetByAdminById(Long questionId);

    /**
     * 题目更新
     */
    Response<Void> questionUpdate(QuestionUpdateRequest questionUpdateRequest);

    /**
     * 题目搜索（分页）
     */
    Response<Page<QuestionVO>> questionSearch(QuestionSearchRequest questionSearchRequest);

    /**
     * question 转换为 questionVO
     */
    QuestionVO domain2VO(Question question);
}

