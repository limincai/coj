package com.mincai.ikuncode.controller;

import com.mincai.ikuncode.annotation.CheckLogin;
import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.exception.BusinessException;
import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionSubmitAddRequest;
import com.mincai.ikuncode.model.enums.ErrorCode;
import com.mincai.ikuncode.service.QuestionSubmitService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 题目提交接口
 *
 * @author limincai
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    /**
     * 提交题目（需要登陆）
     */
    @PostMapping("/add")
    @CheckLogin
    public Response<Long> doSubmitQuestion(HttpSession httpSession, @RequestBody QuestionSubmitAddRequest questionSubmitAddRequest) {
        // 参数校验
        Long questionId = questionSubmitAddRequest.getQuestionId();
        String language = questionSubmitAddRequest.getLanguage();
        String code = questionSubmitAddRequest.getCode();
        if (questionId == null || questionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isAnyBlank(language, code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "必填信息不能为空");
        }
        return questionSubmitService.doSubmitQuestion(httpSession, questionSubmitAddRequest);
    }
}
