package com.mincai.ikuncode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mincai.ikuncode.annotation.CheckLogin;
import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.constant.UserRole;
import com.mincai.ikuncode.exception.BusinessException;
import com.mincai.ikuncode.model.dto.question.*;
import com.mincai.ikuncode.model.enums.ErrorCode;
import com.mincai.ikuncode.model.vo.QuestionAdminVO;
import com.mincai.ikuncode.model.vo.QuestionVO;
import com.mincai.ikuncode.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 题目接口
 *
 * @author limincai
 */
@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {

    //todo 完善题目的增删改查

    @Resource
    private QuestionService questionService;

    /**
     * 添加题目（管理员）
     */
    @PostMapping("/add")
    @CheckLogin(UserRole.ADMIN)
    public Response<Long> questionAdd(@RequestBody QuestionAddRequest questionAddRequest) {
        // 参数校验
        String questionTitle = questionAddRequest.getQuestionTitle();
        String questionDescription = questionAddRequest.getQuestionDescription();
        if (StringUtils.isAnyBlank(questionDescription, questionTitle)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "必填参数不能为空");
        }
        return questionService.questionAdd(questionAddRequest);
    }


    /**
     * 删除题目（管理员）
     */
    @PostMapping("/delete")
    @CheckLogin(UserRole.ADMIN)
    public Response<Void> questionDelete(@RequestBody QuestionDeleteRequest questionDeleteRequest) {
        // 参数校验
        Long deleteQuestionId = questionDeleteRequest.getQuestionId();
        if (deleteQuestionId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "必填参数不能为空");
        }
        if (deleteQuestionId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        return questionService.questionDelete(deleteQuestionId);
    }

    /**
     * 题目列表（分页）管理员
     */
    @PostMapping("/list/admin")
    @CheckLogin(UserRole.ADMIN)
    public Response<Page<QuestionAdminVO>> questionListByAdmin(@RequestBody QuestionListRequest questionListRequest) {
        // 参数校验
        Integer currentPage = questionListRequest.getCurrentPage();
        Integer pageSize = questionListRequest.getPageSize();
        if (currentPage < 0 || pageSize < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return questionService.questionListByAdmin(questionListRequest);
    }


    /**
     * 题目列表（分页）普通用户
     */
    @PostMapping("/list/user")
    public Response<Page<QuestionVO>> questionListByUser(@RequestBody QuestionListRequest questionListRequest) {
        // 参数校验
        Integer currentPage = questionListRequest.getCurrentPage();
        Integer pageSize = questionListRequest.getPageSize();
        if (currentPage < 0 || pageSize < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return questionService.questionListByUser(questionListRequest);
    }


    /**
     * 根据 id 获取题目 VO
     */
    @GetMapping("/{questionId}")
    @CheckLogin()
    public Response<QuestionVO> questionGetById(@PathVariable("questionId") Long questionId) {
        // 参数校验
        if (questionId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return questionService.questionGetById(questionId);
    }

    /**
     * 根据 id 获取题目 AdminVO
     */
    @GetMapping("/{questionId}/admin")
    @CheckLogin(UserRole.ADMIN)
    public Response<QuestionAdminVO> questionGetByAdminById(@PathVariable("questionId") Long questionId) {
        // 参数校验
        if (questionId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return questionService.questionGetByAdminById(questionId);
    }

    /**
     * 题目更新
     */
    @PostMapping("/update")
    @CheckLogin(UserRole.ADMIN)
    public Response<Void> questionUpdate(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        // 参数校验
        Long questionId = questionUpdateRequest.getQuestionId();
        if (questionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return questionService.questionUpdate(questionUpdateRequest);
    }

    /**
     * 题目搜索（分页）
     */
    @PostMapping("/search")
    public Response<Page<QuestionVO>> questionSearch(@RequestBody QuestionSearchRequest questionSearchRequest) {
        // 参数校验
        Integer currentPage = questionSearchRequest.getCurrentPage();
        Integer pageSize = questionSearchRequest.getPageSize();
        if (currentPage < 0 || pageSize < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return questionService.questionSearch(questionSearchRequest);
    }
}
