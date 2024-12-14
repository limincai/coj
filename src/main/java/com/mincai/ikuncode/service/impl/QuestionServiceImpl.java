package com.mincai.ikuncode.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.common.Result;
import com.mincai.ikuncode.exception.BusinessException;
import com.mincai.ikuncode.mapper.QuestionMapper;
import com.mincai.ikuncode.model.domain.Question;
import com.mincai.ikuncode.model.dto.question.*;
import com.mincai.ikuncode.model.enums.ErrorCode;
import com.mincai.ikuncode.model.vo.QuestionAdminVO;
import com.mincai.ikuncode.model.vo.QuestionVO;
import com.mincai.ikuncode.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author limincai
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Resource
    private QuestionMapper questionMapper;

    /**
     * 题目添加
     */
    @Override
    public Response<Long> questionAdd(QuestionAddRequest questionAddRequest) {
        String questionTitle = questionAddRequest.getQuestionTitle();
        String questionDescription = questionAddRequest.getQuestionDescription();
        List<String> questionTags = questionAddRequest.getQuestionTags();
        String questionAnswer = questionAddRequest.getQuestionAnswer();
        List<QuestionJudgeCase> questionJudgeCase = questionAddRequest.getQuestionJudgeCase();
        QuestionJudgeConfig questionJudgeConfig = questionAddRequest.getQuestionJudgeConfig();

        // 参数校验
        // 题目标题不能重复
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getQuestionTitle, questionTitle);
        long count = count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目标题已存在");
        }

        // 过滤出不合法的 judgeCase
        questionJudgeCase = filterQuestionJudgeCase(questionJudgeCase);

        Question question = new Question();
        question.setQuestionTitle(questionTitle);
        question.setQuestionDescription(questionDescription);
        question.setQuestionTags(JSONUtil.toJsonStr(questionTags));
        question.setQuestionAnswer(questionAnswer);
        question.setQuestionJudgeCase(JSONUtil.toJsonStr(questionJudgeCase));
        question.setQuestionJudgeConfig(JSONUtil.toJsonStr(questionJudgeConfig));

        save(question);

        return Result.success(question.getQuestionId());
    }

    /**
     * 题目删除
     */
    @Override
    public Response<Void> questionDelete(Long deleteQuestionId) {
        Question question = getById(deleteQuestionId);
        // 要删除的题目不存在
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 删除题目
        removeById(question);
        return Result.success();
    }

    /**
     * 题目列表（分页）管理员
     */
    @Override
    public Response<Page<QuestionAdminVO>> questionListByAdmin(QuestionListRequest questionListRequest) {
        // 转换 Question 对象为 QuestionAdminVO 对象
        Page<Question> questionPage = getQuestionPage(questionListRequest);
        List<QuestionAdminVO> questionAdminVOList = questionPage.getRecords().stream().map(this::domain2AdminVO).collect(Collectors.toList());

        // 将转换后的结果放入新的 Page<QuestionVO> 中
        Page<QuestionAdminVO> questionAdminPage = new Page<>();
        questionAdminPage.setRecords(questionAdminVOList);
        questionAdminPage.setTotal(questionPage.getTotal());
        questionAdminPage.setCurrent(questionPage.getCurrent());
        questionAdminPage.setSize(questionPage.getSize());

        return Result.success(questionAdminPage);
    }

    /**
     * 题目列表（分页）普通用户
     */
    @Override
    public Response<Page<QuestionVO>> questionListByUser(QuestionListRequest questionListRequest) {
        // 获取题目分页对象
        Page<Question> questionPage = getQuestionPage(questionListRequest);

        // 转换 Question 对象为 QuestionVO 对象
        return Result.success(questionPage2QuestionVOPage(questionPage));
    }

    /**
     * 根据 id 获取题目
     */
    @Override
    public Response<QuestionVO> questionGetById(Long questionId) {
        Question question = getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return Result.success(domain2VO(question));
    }

    /**
     * 根据 id 获取题目（管理员）
     */
    @Override
    public Response<QuestionAdminVO> questionGetByAdminById(Long questionId) {
        Question question = getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return Result.success(domain2AdminVO(question));
    }

    @Override
    public Response<Void> questionUpdate(QuestionUpdateRequest questionUpdateRequest) {
        Long questionId = questionUpdateRequest.getQuestionId();
        String questionTitle = questionUpdateRequest.getQuestionTitle();
        String questionDescription = questionUpdateRequest.getQuestionDescription();
        List<String> questionTags = questionUpdateRequest.getQuestionTags();
        String questionAnswer = questionUpdateRequest.getQuestionAnswer();
        List<QuestionJudgeCase> questionJudgeCase = questionUpdateRequest.getQuestionJudgeCase();
        QuestionJudgeConfig questionJudgeConfig = questionUpdateRequest.getQuestionJudgeConfig();

        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getQuestionTitle, questionTitle);
        Question question = getOne(queryWrapper);
        if (question != null && !question.getQuestionId().equals(questionUpdateRequest.getQuestionId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目标题已存在");
        }

        questionJudgeCase = filterQuestionJudgeCase(questionJudgeCase);

        question = new Question();
        question.setQuestionId(questionId);
        question.setQuestionTitle(questionTitle);
        question.setQuestionDescription(questionDescription);
        question.setQuestionAnswer(questionAnswer);
        question.setQuestionJudgeCase(JSONUtil.toJsonStr(questionJudgeCase));
        question.setQuestionTags(JSONUtil.toJsonStr(questionTags));
        question.setQuestionJudgeConfig(JSONUtil.toJsonStr(questionJudgeConfig));

        updateById(question);
        return Result.success();
    }

    /**
     * 题目搜索（分页）
     */
    @Override
    public Response<Page<QuestionVO>> questionSearch(QuestionSearchRequest questionSearchRequest) {
        String searchKeyword = questionSearchRequest.getSearchKeyword();
        Integer currentPage = questionSearchRequest.getCurrentPage();
        Integer pageSize = questionSearchRequest.getPageSize();

        // 如果当前搜索关键字为空，直接返回题目列表
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            QuestionListRequest questionListRequest = new QuestionListRequest();
            questionListRequest.setCurrentPage(questionSearchRequest.getCurrentPage());
            questionListRequest.setPageSize(questionSearchRequest.getPageSize());
            return questionListByUser(questionListRequest);
        }

        // 构造分页对象
        Page<Question> questionPage = new Page<>(currentPage, pageSize);

        // 构造查询参数
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                // 搜索题目 id
                .or(q -> q.like(Question::getQuestionId, searchKeyword))
                // 搜索题目标题
                .or(q -> q.like(Question::getQuestionTitle, searchKeyword));

        // 查询符合 题目 id 和 题目 title 的题目
        questionPage = page(questionPage, queryWrapper);

        // 查询符合 Tags 条件的记录
        List<Question> tagFilteredQuestions = questionMapper.selectList(null).stream().filter(question -> {
            // JSON 字符串解析为 List<String>
            List<String> tagsInDb = JSONUtil.toList(question.getQuestionTags(), String.class);
            if (CollUtil.isNotEmpty(tagsInDb)) {
                // 判断 tags 中是否包含搜索关键字
                return tagsInDb.stream().anyMatch(tag -> StrUtil.containsIgnoreCase(tag, searchKeyword));
            }
            return false;
        }).collect(Collectors.toList());


        // 合并查询结果并去重
        List<Question> mergedResults = CollUtil.newArrayList(questionPage.getRecords());
        mergedResults.addAll(tagFilteredQuestions);
        mergedResults = mergedResults.stream()
                // 去重
                .distinct()
                // 根据 questionId 升序
                .sorted(Comparator.comparing(Question::getQuestionId)).collect(Collectors.toList());

        // 构造新的分页结果
        questionPage.setTotal(mergedResults.size());
        questionPage.setRecords(mergedResults.stream().skip((long) (currentPage - 1) * pageSize).limit(pageSize).collect(Collectors.toList()));


        // 将 questionPage 转换为 questionVOPage
        return Result.success(questionPage2QuestionVOPage(questionPage));
    }


    /**
     * 将 questionPage 中的 question 脱敏为 questionPage
     */
    private Page<QuestionVO> questionPage2QuestionVOPage(Page<Question> questionPage) {
        List<QuestionVO> questionVOList = questionPage.getRecords().stream().map(this::domain2VO).collect(Collectors.toList());

        // 将转换后的结果放入新的 Page<QuestionVO> 中
        Page<QuestionVO> questionVOPage = new Page<>();
        questionVOPage.setRecords(questionVOList);
        questionVOPage.setTotal(questionPage.getTotal());
        questionVOPage.setCurrent(questionPage.getCurrent());
        questionVOPage.setSize(questionPage.getSize());

        return questionVOPage;
    }

    /**
     * 过滤出不合法的 judgeCase
     * 只允许第一个 judgeCase 的 input 和  output 可以为空字符串
     * 当 input 为空字符串，output 不为空字符串时，全部设置为空字符串，反之亦然
     */
    private List<QuestionJudgeCase> filterQuestionJudgeCase(List<QuestionJudgeCase> judgeCase) {
        // 处理输入和输出全为空的情况
        judgeCase.forEach(caseItem -> {
            if (caseItem.getInput().isEmpty() && !caseItem.getOutput().isEmpty()) {
                caseItem.setInput("");
                caseItem.setOutput("");
            } else if (caseItem.getOutput().isEmpty() && !caseItem.getInput().isEmpty()) {
                caseItem.setInput("");
                caseItem.setOutput("");
            }
        });

        // 保留第一个空的 judgeCase，移除其他空的 judgeCase
        // 用于记录是否已找到一个空的 case
        boolean[] hasEmptyCase = {false};
        return judgeCase.stream().filter(caseItem -> {
            boolean isEmpty = caseItem.getInput().isEmpty() && caseItem.getOutput().isEmpty();
            if (isEmpty) {
                if (!hasEmptyCase[0]) {
                    hasEmptyCase[0] = true;
                    return true; // 保留第一个空 case
                }
                return false; // 移除多余的空 case
            }
            return true; // 保留非空 case
        }).collect(Collectors.toList());
    }


    /**
     * 获取题目分页对象
     */
    private Page<Question> getQuestionPage(QuestionListRequest questionListRequest) {
        Integer currentPage = questionListRequest.getCurrentPage();
        Integer pageSize = questionListRequest.getPageSize();

        // 创建分页对象
        Page<Question> page = new Page<>(currentPage, pageSize);

        // 按照 id 升序排序
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Question::getQuestionId);

        // 执行分页查询
        return page(page, queryWrapper);
    }


    /**
     * question 转换为 questionVO
     */
    @Override
    public QuestionVO domain2VO(Question question) {
        Long questionId = question.getQuestionId();
        String questionTitle = question.getQuestionTitle();
        String questionDescription = question.getQuestionDescription();
        String questionTags = question.getQuestionTags();
        Long questionSubmitNum = question.getQuestionSubmitNum();
        Long questionAcceptedNum = question.getQuestionAcceptedNum();
        String questionJudgeConfig = question.getQuestionJudgeConfig();
        Date createTime = question.getCreateTime();


        QuestionVO questionVO = new QuestionVO();
        questionVO.setQuestionId(questionId);
        questionVO.setQuestionTitle(questionTitle);
        questionVO.setQuestionDescription(questionDescription);
        questionVO.setQuestionTags(JSONUtil.toList(questionTags, String.class));
        questionVO.setQuestionSubmitNum(questionSubmitNum);
        questionVO.setQuestionAcceptedNum(questionAcceptedNum);
        QuestionJudgeConfig questionVOJudgeConfig = JSONUtil.toBean(questionJudgeConfig, QuestionJudgeConfig.class);
        if (questionVOJudgeConfig.getTimeLimit() == null) {
            questionVOJudgeConfig.setTimeLimit(0L);
        }
        if (questionVOJudgeConfig.getMemoryLimit() == null) {
            questionVOJudgeConfig.setMemoryLimit(0L);
        }
        questionVO.setQuestionJudgeConfig(questionVOJudgeConfig);
        questionVO.setCreateTime(createTime);

        return questionVO;
    }

    /**
     * question 转换为 questionAdminVO
     */
    private QuestionAdminVO domain2AdminVO(Question question) {
        Long questionId = question.getQuestionId();
        String questionTitle = question.getQuestionTitle();
        String questionDescription = question.getQuestionDescription();
        String questionTags = question.getQuestionTags();
        String questionAnswer = question.getQuestionAnswer();
        Long questionSubmitNum = question.getQuestionSubmitNum();
        Long questionAcceptedNum = question.getQuestionAcceptedNum();
        String questionJudgeCase = question.getQuestionJudgeCase();
        String questionJudgeConfig = question.getQuestionJudgeConfig();
        Date createTime = question.getCreateTime();
        Date updateTime = question.getUpdateTime();

        QuestionAdminVO questionAdminVO = new QuestionAdminVO();
        questionAdminVO.setQuestionId(questionId);
        questionAdminVO.setQuestionTags(JSONUtil.toList(questionTags, String.class));
        questionAdminVO.setQuestionTitle(questionTitle);
        questionAdminVO.setQuestionDescription(questionDescription);
        questionAdminVO.setQuestionAnswer(questionAnswer);
        questionAdminVO.setQuestionSubmitNum(questionSubmitNum);
        questionAdminVO.setQuestionAcceptedNum(questionAcceptedNum);
        questionAdminVO.setQuestionJudgeCase(JSONUtil.toList(questionJudgeCase, QuestionJudgeCase.class));
        QuestionJudgeConfig questionAdminVOJudgeConfig = JSONUtil.toBean(questionJudgeConfig, QuestionJudgeConfig.class);
        if (questionAdminVOJudgeConfig.getTimeLimit() == null) {
            questionAdminVOJudgeConfig.setTimeLimit(0L);
        }
        if (questionAdminVOJudgeConfig.getMemoryLimit() == null) {
            questionAdminVOJudgeConfig.setMemoryLimit(0L);
        }
        questionAdminVO.setQuestionJudgeConfig(questionAdminVOJudgeConfig);
        questionAdminVO.setQuestionJudgeConfig(JSONUtil.toBean(questionJudgeConfig, QuestionJudgeConfig.class));
        questionAdminVO.setCreateTime(createTime);
        questionAdminVO.setUpdateTime(updateTime);


        return questionAdminVO;
    }
}




