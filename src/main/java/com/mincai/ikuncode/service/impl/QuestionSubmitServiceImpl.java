package com.mincai.ikuncode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.common.Result;
import com.mincai.ikuncode.constant.QuestionSubmitStatus;
import com.mincai.ikuncode.constant.UserConstant;
import com.mincai.ikuncode.exception.BusinessException;
import com.mincai.ikuncode.mapper.QuestionSubmitMapper;
import com.mincai.ikuncode.model.domain.Question;
import com.mincai.ikuncode.model.domain.QuestionSubmit;
import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionSubmitAddRequest;
import com.mincai.ikuncode.model.enums.ErrorCode;
import com.mincai.ikuncode.model.vo.UserVO;
import com.mincai.ikuncode.service.QuestionService;
import com.mincai.ikuncode.service.QuestionSubmitService;
import com.mincai.ikuncode.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author limincai
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit> implements QuestionSubmitService {

    @Resource
    UserService userService;

    @Resource
    QuestionService questionService;

    /**
     * 题目提交
     */
    @Override
    public Response<Long> doSubmitQuestion(HttpSession session, QuestionSubmitAddRequest questionSubmitAddRequest) {
        // 当前登陆用户
        UserVO loginUserVO = (UserVO) session.getAttribute(UserConstant.USER_LOGIN_STATE);

        // todo 参数校验
        Long questionId = questionSubmitAddRequest.getQuestionId();
        String language = questionSubmitAddRequest.getLanguage();
        String code = questionSubmitAddRequest.getCode();

        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        //todo 判题

        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(loginUserVO.getUserId());
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setLanguage(language);
        questionSubmit.setCode(code);
        questionSubmit.setStatus(QuestionSubmitStatus.WAITING);
        questionSubmit.setQuestionJudgeInfo("{}");

        save(questionSubmit);

        return Result.success(questionSubmit.getQuestionSubmitId());
    }
}




