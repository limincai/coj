package com.mincai.ikuncode.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.common.Result;
import com.mincai.ikuncode.constant.QuestionJudgeInfoMessage;
import com.mincai.ikuncode.constant.QuestionSubmitStatus;
import com.mincai.ikuncode.constant.UserConstant;
import com.mincai.ikuncode.exception.BusinessException;
import com.mincai.ikuncode.judge.JudgeService;
import com.mincai.ikuncode.mapper.QuestionSubmitMapper;
import com.mincai.ikuncode.model.domain.Question;
import com.mincai.ikuncode.model.domain.QuestionSubmit;
import com.mincai.ikuncode.model.domain.User;
import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionJudgeInfo;
import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionSubmitAddRequest;
import com.mincai.ikuncode.model.dto.questisonsubmit.QuestionSubmitListRequest;
import com.mincai.ikuncode.model.enums.ErrorCode;
import com.mincai.ikuncode.model.vo.QuestionSubmitVO;
import com.mincai.ikuncode.model.vo.UserVO;
import com.mincai.ikuncode.service.QuestionService;
import com.mincai.ikuncode.service.QuestionSubmitService;
import com.mincai.ikuncode.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author limincai
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit> implements QuestionSubmitService {

    @Resource
    UserService userService;

    @Resource
    QuestionService questionService;

    @Resource
    @Lazy
    JudgeService judgeService;


    /**
     * 题目提交
     */
    @Override
    public Response<QuestionJudgeInfo> doSubmitQuestion(HttpSession session, QuestionSubmitAddRequest questionSubmitAddRequest) {
        // 当前登陆用户
        UserVO loginUserVO = (UserVO) session.getAttribute(UserConstant.USER_LOGIN_STATE);

        Long userId = questionSubmitAddRequest.getUserId();
        Long questionId = questionSubmitAddRequest.getQuestionId();
        String language = questionSubmitAddRequest.getLanguage();
        String code = questionSubmitAddRequest.getCode();
        User user = userService.getById(userId);

        // 题目为空
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 初始化题目提交
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(loginUserVO.getUserId());
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setLanguage(language);
        questionSubmit.setCode(code);
        questionSubmit.setStatus(QuestionSubmitStatus.WAITING);
        questionSubmit.setUserId(userId);
        save(questionSubmit);

        // 判题机判题
        QuestionJudgeInfo questionJudgeInfo = judgeService.doJudge(questionSubmit.getQuestionSubmitId());
        if (questionJudgeInfo.getMessage().equals(QuestionJudgeInfoMessage.ACCEPTED)) {
            // ac 数量 + 1
            Question updateQuestion = new Question();
            updateQuestion.setQuestionId(questionId);
            updateQuestion.setQuestionAcceptedNum(question.getQuestionAcceptedNum() + 1);
            questionService.updateById(updateQuestion);
            // 鸡脚 + 1
            User updateUser = new User();
            updateUser.setUserJijiao(user.getUserJijiao() + 1);
            updateUser.setUserId(userId);
            userService.updateById(updateUser);
        }
        QuestionSubmit updatedQuestionSubmit = new QuestionSubmit();
        updatedQuestionSubmit.setQuestionJudgeInfo(JSONUtil.toJsonStr(questionJudgeInfo));
        updateById(updatedQuestionSubmit);

        // 题目提交数 + 1
        question.setQuestionSubmitNum(question.getQuestionSubmitNum() + 1);
        questionService.updateById(question);

        return Result.success(questionJudgeInfo);
    }

    @Override
    public Response<Page<QuestionSubmitVO>> listQuestionSubmitVO(QuestionSubmitListRequest questionSubmitListRequest) {

        Integer currentPage = questionSubmitListRequest.getCurrentPage();
        Integer pageSize = questionSubmitListRequest.getPageSize();

        // 创建分页对象
        Page<QuestionSubmit> questionSubmitPage = new Page<>(currentPage, pageSize);

        // 按照创建时间升序排序
        LambdaQueryWrapper<QuestionSubmit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(QuestionSubmit::getCreateTime);

        // 执行分页查询
        questionSubmitPage = page(questionSubmitPage, queryWrapper);

        // 转换 Question 对象为 QuestionVO 对象
        return Result.success(questionSubmitPage2QuestionSubmitVOPage(questionSubmitPage));
    }

    @Override
    public Response<Page<QuestionSubmitVO>> listQuestionSubmitVOByUserIdByQuestionId(QuestionSubmitListRequest questionSubmitListRequest) {
        Long userId = questionSubmitListRequest.getUserId();
        Long questionId = questionSubmitListRequest.getQuestionId();
        Integer currentPage = questionSubmitListRequest.getCurrentPage();
        Integer pageSize = questionSubmitListRequest.getPageSize();


        // 构建分页对象
        Page<QuestionSubmit> questionSubmitPage = new Page<>(currentPage, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<QuestionSubmit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestionSubmit::getQuestionId, questionId).eq(QuestionSubmit::getUserId, userId).orderByDesc(QuestionSubmit::getCreateTime);

        questionSubmitPage = page(questionSubmitPage, queryWrapper);
        return Result.success(questionSubmitPage2QuestionSubmitVOPage(questionSubmitPage));
    }

    /**
     * 将 questionPage 中的 question 脱敏为 questionPage
     */
    private Page<QuestionSubmitVO> questionSubmitPage2QuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage) {
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitPage.getRecords().stream().map(this::domain2VO).collect(Collectors.toList());

        // 将转换后的结果放入新的 Page<QuestionVO> 中
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>();
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        questionSubmitVOPage.setTotal(questionSubmitPage.getTotal());
        questionSubmitVOPage.setCurrent(questionSubmitPage.getCurrent());
        questionSubmitVOPage.setSize(questionSubmitPage.getSize());

        return questionSubmitVOPage;
    }

    private QuestionSubmitVO domain2VO(QuestionSubmit questionSubmit) {
        Long questionSubmitId = questionSubmit.getQuestionSubmitId();
        Long userId = questionSubmit.getUserId();
        Long questionId = questionSubmit.getQuestionId();
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        String questionJudgeInfoStr = questionSubmit.getQuestionJudgeInfo();
        Integer status = questionSubmit.getStatus();
        Date createTime = questionSubmit.getCreateTime();

        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        questionSubmitVO.setQuestionSubmitId(questionSubmitId);
        questionSubmitVO.setUserVO(userService.domain2VO(userService.getById(userId)));
        questionSubmitVO.setQuestionVO(questionService.domain2VO(questionService.getById(questionId)));
        questionSubmitVO.setLanguage(language);
        questionSubmitVO.setCode(code);
        questionSubmitVO.setQuestionJudgeInfo(JSONUtil.toBean(questionJudgeInfoStr, QuestionJudgeInfo.class));
        questionSubmitVO.setStatus(status);
        questionSubmitVO.setCreateTime(createTime);

        return questionSubmitVO;
    }
}




