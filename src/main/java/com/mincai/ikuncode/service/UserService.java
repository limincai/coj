package com.mincai.ikuncode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.model.domain.User;
import com.mincai.ikuncode.model.dto.UserDTO;
import com.mincai.ikuncode.model.vo.UserVO;
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
    Response<Long> userRegister(UserDTO userDTO);

    /**
     * 用户登陆
     */
    Response<UserVO> userLogin(HttpSession session, UserDTO userDTO);

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
    Response<UserVO> userUpdate(HttpSession session, UserVO loginUserVO, UserVO updateUserVO);

    /**
     * 用户上传头像
     */
    String uploadAvatar(MultipartFile multipartFile, Long loginUserId) throws IOException;

    /**
     * 获取保存的用户信息
     */
    Response<UserVO> getLoginUserVO(HttpSession session);
}
