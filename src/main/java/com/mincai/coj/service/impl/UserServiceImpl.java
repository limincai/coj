package com.mincai.coj.service.impl;

import cn.hutool.core.lang.UUID;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.mincai.coj.config.OSSProperties;
import com.mincai.coj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author limincai
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    OSSProperties ossProperties;

    private final static String IMG_DIRECTORY = "img/";

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
}
