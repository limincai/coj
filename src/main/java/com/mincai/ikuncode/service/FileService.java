package com.mincai.ikuncode.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author limincai
 */
public interface FileService {

    /**
     * 用户上传头像
     */
    String uploadUserAvatar(MultipartFile multipartFile, HttpSession session, Long loginUserId) throws IOException;
}
