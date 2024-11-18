package com.mincai.coj.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author limincai
 */
public interface UserService {

    String uploadImg(MultipartFile multipartFile) throws IOException;
}
