package com.mincai.coj.service.impl;

import cn.hutool.core.lang.UUID;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mincai.coj.common.Response;
import com.mincai.coj.common.Result;
import com.mincai.coj.config.OSSProperties;
import com.mincai.coj.constant.UserConstant;
import com.mincai.coj.enums.ErrorCode;
import com.mincai.coj.exception.BusinessException;
import com.mincai.coj.mapper.UserMapper;
import com.mincai.coj.model.domain.User;
import com.mincai.coj.model.dto.UserDTO;
import com.mincai.coj.model.vo.UserVO;
import com.mincai.coj.service.UserService;
import com.mincai.coj.utils.RegUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * @author limincai
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    OSSProperties ossProperties;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    UserMapper userMapper;


    /**
     * 单次上传文件最大大小：5MB
     */
    static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * oss 保存图片文件夹
     */
    private final static String IMG_DIRECTORY = "img/";

    /**
     * 用户注册
     */
    @Override
    public Response<Integer> userRegister(UserDTO userDTO) {
        String userAccount = userDTO.getUserAccount();
        String userPassword = userDTO.getUserPassword();
        String userConfirmedPassword = userDTO.getUserConfirmedPassword();

        // 参数校验
        // 账号为 8 - 16 位不允许带特殊字符
        if (!RegUtil.isLegalUserAccount(userAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码格式错误");
        }
        // 密码为 8 - 16 位不允许带特殊字符
        if (!RegUtil.isLegalUserPassword(userAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码格式错误");
        }
        // 密码与确认密码一致
        if (!userPassword.equals(userConfirmedPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码与却惹密码不一致");
        }
        // 查询数据库是否有相同账号的用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名已存在");
        }

        // todo 用户密码加密

        // 插入用户
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(userPassword);
        save(user);

        return Result.success(user.getUserId());
    }

    /**
     * 用户删除
     *
     * @param userDTO 要求为管理员
     * @return
     */
    @Override
    public Response<UserVO> userLogin(HttpSession session, UserDTO userDTO) {
        String userAccount = userDTO.getUserAccount();
        String userPassword = userDTO.getUserPassword();

        // 参数校验
        if (!RegUtil.isLegalUserAccount(userAccount) || !RegUtil.isLegalUserPassword(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码格式错误");
        }

        // 查询数据库账号是否存在或密码输入错误
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount).eq(User::getUserPassword, userPassword);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不存在或密码错误");
        }

        // 是否为封禁用户
        if (user.getIsDeleted().equals(UserConstant.USER_BANED_CODE)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "账号已被封禁");
        }

        // 将用户转为 userVO，进行脱敏
        UserVO userVO = domain2Dto(user);

        // 记录用户的登陆态
        session.setAttribute(UserConstant.USER_LOGIN_STATE, userVO);

        return Result.success(userVO);
    }

    @Override
    public Response<Void> userLogout(HttpSession session) {
        //todo 删除 redis 中的缓存

        // 删除用户登陆态
        session.removeAttribute(UserConstant.USER_LOGIN_STATE);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @Override
    public Response<Void> userDelete(UserVO loginUserVO, Integer deleteUserId) {
        // 是否为超级管理员或管理员
        Integer loginUserRole = loginUserVO.getUserRole();
        if (!loginUserRole.equals(UserConstant.SUPER_ADMIN_ROLE) && !loginUserRole.equals(UserConstant.ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 当前用户是否存在
        User deleteUser = getById(deleteUserId);
        if (deleteUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 不能删除自己
        Integer deleteUserRole = deleteUser.getUserRole();
        if (loginUserVO.getUserId().equals(deleteUserId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "😡，还想逃");
        }

        // 管理员不能删除管理员
        if (loginUserRole.equals(UserConstant.ADMIN_ROLE) && deleteUserRole.equals(UserConstant.ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        //超级管理员有最高权限
        if (deleteUserRole.equals(UserConstant.SUPER_ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 删除用户
        removeById(deleteUserId);
        return Result.success();
    }

    /**
     * 用户修改
     */
    @Override
    public Response<UserVO> userUpdate(HttpSession session, UserVO loginUserVO, UserVO updateUserVO) {
        // 参数校验
        Integer userId = updateUserVO.getUserId();
        String userNickname = updateUserVO.getUserNickname();

        // 用户不能修改自己的权限
        if (!loginUserVO.getUserRole().equals(updateUserVO.getUserRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 昵称少于 20 位
        if (userNickname.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "昵称过长");
        }

        User updateUser = new User();
        updateUser.setUserId(userId);
        updateUser.setUserNickname(userNickname);
        updateById(updateUser);

        // 将更新后的用户存入 session 中
        loginUserVO.setUserNickname(userNickname);
        session.setAttribute(UserConstant.USER_LOGIN_STATE, loginUserVO);
        return Result.success(loginUserVO);
    }


    @Override
    public String uploadImg(MultipartFile multipartFile) throws IOException {
        // todo 压缩图片

        // 原文件名
        String originFileName = multipartFile.getOriginalFilename();

        // 上传的文件名
        String fileName = IMG_DIRECTORY + UUID.randomUUID() + originFileName.substring(originFileName.lastIndexOf("."));

        // 上传文件
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKey(), ossProperties.getSecretAccessKey());
        ossClient.putObject(
                //仓库名
                "limincai-coj",
                // 文件名
                fileName,
                // 原文件
                multipartFile.getInputStream());

        //关闭客户端
        ossClient.shutdown();

        // 返回访问路径
        return ossProperties.getBucket() + fileName;
    }

    private UserVO domain2Dto(User user) {
        Integer userId = user.getUserId();
        Integer userRole = user.getUserRole();
        String userNickname = user.getUserNickname();
        String userAvatarUrl = user.getUserAvatarUrl();
        Date createTime = user.getCreateTime();

        UserVO userVO = new UserVO();
        userVO.setUserId(userId);
        userVO.setUserRole(userRole);
        userVO.setUserNickname(userNickname);
        userVO.setUserAvatarUrl(userAvatarUrl);
        userVO.setCreateTime(createTime);
        return userVO;
    }

}
