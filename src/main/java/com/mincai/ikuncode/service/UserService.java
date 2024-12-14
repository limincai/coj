package com.mincai.ikuncode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.model.domain.User;
import com.mincai.ikuncode.model.dto.user.UserLoginRequest;
import com.mincai.ikuncode.model.dto.user.UserRegisterRequest;
import com.mincai.ikuncode.model.dto.user.UserRetrievePasswordRequest;
import com.mincai.ikuncode.model.dto.user.UserUpdateRequest;
import com.mincai.ikuncode.model.vo.UserVO;

import javax.servlet.http.HttpSession;

/**
 * @author limincai
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    Response<Long> userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登陆
     */
    Response<UserVO> userLogin(HttpSession session, UserLoginRequest userLoginRequest);

    /**
     * 用户登出
     */
    Response<Void> userLogout(HttpSession session);

    /**
     * 用户删除（要求为管理员）
     */
    Response<Void> userDelete(UserVO loginUserVO, Long deleteUserId);

    /**
     * 用户修改
     */
    Response<UserVO> userUpdate(HttpSession session, UserUpdateRequest userUpdateRequest);


    /**
     * 获取保存的用户信息
     */
    Response<UserVO> getLoginUserVO(HttpSession session);

    /**
     * 用户找回密码
     */
    Response<Void> userRetrievePassword(UserRetrievePasswordRequest userRetrievePasswordRequest);


    UserVO domain2Dto(User user);

    /**
     * 根据 id 获取用户
     */
    Response<UserVO> userGetById(Long userId);
}
