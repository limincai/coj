package com.mincai.coj.controller;


import com.mincai.coj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

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


    @PostMapping
    public String upload(MultipartFile multipartFile) {
        String fileName;
        try {
            fileName = userService.uploadImg(multipartFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }
}