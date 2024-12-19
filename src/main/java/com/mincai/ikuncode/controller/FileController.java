package com.mincai.ikuncode.controller;

import com.mincai.ikuncode.annotation.CheckLogin;
import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.common.Result;
import com.mincai.ikuncode.constant.UserConstant;
import com.mincai.ikuncode.exception.BusinessException;
import com.mincai.ikuncode.model.enums.ErrorCode;
import com.mincai.ikuncode.model.vo.UserVO;
import com.mincai.ikuncode.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 文件接口
 *
 * @author limincai
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private FileService fileService;

    /**
     * 用户上传头像
     *
     * @return 用户头像的 url
     */
    @PostMapping("/upload-user-avatar")
    @CheckLogin()
    public Response<String> uploadUserAvatar(MultipartFile file, HttpSession session) {
        // 登陆用户才能上传头像
        UserVO loginUserVo = (UserVO) session.getAttribute(UserConstant.USER_LOGIN_STATE);
        if (loginUserVo == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        String fileName;
        try {
            fileName = fileService.uploadUserAvatar(file, session, loginUserVo.getUserId());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统出错，请重试");
        }
        return Result.success(fileName);
    }

    /**
     * 上传图片到服务器
     *
     * @return 图片的url
     */
    @PostMapping("/upload-img")
    @CheckLogin()
    public Response<String> uploadUserAvatar(MultipartFile file) {
        String fileName;
        try {
            fileName = fileService.uploadImg(file);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统出错，请重试");
        }
        return Result.success(fileName);
    }
}
