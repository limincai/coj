package com.mincai.coj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mincai.coj.common.Response;
import com.mincai.coj.model.domain.User;
import com.mincai.coj.model.dto.UserDTO;
import com.mincai.coj.model.vo.UserVO;
import io.swagger.models.auth.In;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author limincai
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    Response<Integer> userRegister(UserDTO userDTO);

    /**
     * 用户登陆
     */
    Response<UserVO> userLogin(HttpSession session, UserDTO userDTO);

    /**
     * 用户登出
     */
    Response<Void> userLogout(HttpSession session);

    /**
     * 用户删除
     */
    Response<Void> userDelete(UserVO loginUserVO, Integer deleteUserId);

    /**
     * 用户修改
     */
    Response<UserVO> userUpdate(HttpSession session, UserVO loginUserVO, UserVO updateUserVO);

    /**
     * 用户上传头像
     */
    String uploadAvatar(MultipartFile multipartFile,Integer loginUserId) throws IOException;
}
