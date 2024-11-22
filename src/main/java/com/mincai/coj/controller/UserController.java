package com.mincai.coj.controller;


import com.mincai.coj.common.Response;
import com.mincai.coj.common.Result;
import com.mincai.coj.constant.UserConstant;
import com.mincai.coj.enums.ErrorCode;
import com.mincai.coj.exception.BusinessException;
import com.mincai.coj.model.dto.UserDTO;
import com.mincai.coj.model.vo.UserVO;
import com.mincai.coj.service.UserService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * 用户接口
 *
 * @author limincai
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    // todo 自定义注解，对需要登陆和权限的接口进行切面处理

    // todo 整合 so-token，简化权限校验

    /**
     * 用户注册
     *
     * @param userDTO 账号为 8 - 16 位不允许带特殊字符；密码为 8 - 16 位不允许带特殊字符
     */
    @PostMapping("/register")
    public Response<Integer> userRegister(@RequestBody UserDTO userDTO) {
        // 参数校验
        String userAccount = userDTO.getUserAccount();
        String userEmail = userDTO.getUserEmail();
        String getCaptcha = userDTO.getCaptcha();
        String userPassword = userDTO.getUserPassword();
        String userConfirmedPassword = userDTO.getUserConfirmedPassword();
        if (StringUtils.isAnyBlank(userAccount, userEmail, userPassword, userConfirmedPassword, getCaptcha)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "必填信息不能为空");
        }
        return userService.userRegister(userDTO);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Response<UserVO> userLogin(HttpSession session, @RequestBody UserDTO userDTO) {
        // 参数校验
        String userAccount = userDTO.getUserAccount();
        String userPassword = userDTO.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码未输入");
        }
        return userService.userLogin(session, userDTO);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Response<Void> userLogout(HttpSession session) {
        // 参数校验
        UserVO loginUserVO = (UserVO) session.getAttribute(UserConstant.USER_LOGIN_STATE);
        if (loginUserVO == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return userService.userLogout(session);
    }


    /**
     * 删除用户
     * 管理员才能删除用户；管理员不能删除管理员；超级管理员有超级权限
     */
    @PostMapping("/delete")
    public Response<Void> userDelete(HttpSession session, @RequestBody UserDTO userDTO) {
        // 参数校验
        UserVO loginUserVO = (UserVO) session.getAttribute(UserConstant.USER_LOGIN_STATE);
        if (loginUserVO == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        Integer deleteUserId = userDTO.getUserId();
        if (deleteUserId == null || deleteUserId < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userService.userDelete(loginUserVO, deleteUserId);
    }

    /**
     * 用户修改
     * 用户自己修改自己
     */
    @PostMapping("/update")
    public Response<UserVO> userUpdate(HttpSession session, @RequestBody UserVO updateUserVO) {
        // 参数校验
        UserVO loginUserVO = (UserVO) session.getAttribute(UserConstant.USER_LOGIN_STATE);
        if (loginUserVO == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return userService.userUpdate(session, loginUserVO, updateUserVO);
    }

    // todo 用户找回密码

    // todo 用户上传头像
    @PostMapping("/upload_avatar")
    public Response<String> uploadAvatar(MultipartFile multipartFile, HttpSession session) {
        // 登陆用户才能上传头像
        UserVO loginUserVo = (UserVO) session.getAttribute(UserConstant.USER_LOGIN_STATE);
        if (loginUserVo == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        String fileName;
        try {
            fileName = userService.uploadAvatar(multipartFile, loginUserVo.getUserId());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统出错，请重试");
        }
        return Result.success(fileName);
    }
}