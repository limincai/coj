package com.mincai.ikuncode.service.impl;

import cn.hutool.core.lang.UUID;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.mincai.ikuncode.config.OSSProperties;
import com.mincai.ikuncode.constant.UserConstant;
import com.mincai.ikuncode.exception.BusinessException;
import com.mincai.ikuncode.model.domain.User;
import com.mincai.ikuncode.model.enums.ErrorCode;
import com.mincai.ikuncode.model.vo.UserVO;
import com.mincai.ikuncode.service.FileService;
import com.mincai.ikuncode.service.UserService;
import com.mincai.ikuncode.utils.RegUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author limincai
 */
@Service
public class FileServiceImpl implements FileService {


    /**
     * oss 保存图片文件夹
     */
    private final static String IMG_DIRECTORY = "img/";

    @Resource
    OSSProperties ossProperties;

    @Resource
    UserService userService;


    /**
     * 上传用户头像
     */
    @Override
    public String uploadUserAvatar(MultipartFile file, HttpSession session, Long loginUserId) throws IOException {
        // 原文件名
        String originFileName = file.getOriginalFilename();

        // 验证文件名是否正确
        if (!RegUtil.isLegalPictureFormat(originFileName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件格式不正确，请重试");
        }

        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKey(), ossProperties.getSecretAccessKey());

        // 删除原来的头像
        UserVO loginUserVO = (UserVO) session.getAttribute(UserConstant.USER_LOGIN_STATE);
        String originUserAvatarUrl = loginUserVO.getUserAvatarUrl();
        if (!originUserAvatarUrl.isEmpty()) {
            ossClient.deleteObject(
                    // bucket
                    ossProperties.getBucketName(),
                    // 文件名
                    originUserAvatarUrl.substring(loginUserVO.getUserAvatarUrl().indexOf("img/")));
        }

        // 上传的文件名
        String fileName = IMG_DIRECTORY + UUID.randomUUID() + originFileName.substring(originFileName.lastIndexOf("."));

        // 上传文件
        ossClient.putObject(
                // 桶名
                ossProperties.getBucketName(),
                // 文件名
                fileName,
                // 原文件
                file.getInputStream());

        //关闭客户端
        ossClient.shutdown();

        // 保存头像地址到数据库
        String avatarUrl = ossProperties.getPrefix() + fileName;
        User user = new User();
        user.setUserId(loginUserId);
        user.setUserAvatarUrl(avatarUrl);
        userService.updateById(user);

        // 更新用户登陆态
        session.setAttribute(UserConstant.USER_LOGIN_STATE, userService.domain2VO(userService.getById(loginUserId)));

        // 返回访问路径
        return ossProperties.getPrefix() + fileName;
    }
}
