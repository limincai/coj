package com.mincai.ikuncode.controller;

import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.constant.EmailConstant;
import com.mincai.ikuncode.constant.EmailSubject;
import com.mincai.ikuncode.exception.BusinessException;
import com.mincai.ikuncode.model.dto.email.GetEmailRequest;
import com.mincai.ikuncode.model.enums.ErrorCode;
import com.mincai.ikuncode.service.EmailService;
import com.mincai.ikuncode.utils.RegUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author limincai
 */
@RestController
@RequestMapping("/email")
@Slf4j
public class EmailController {

    @Resource
    EmailService emailService;

    /**
     * 发送注册邮箱验证码
     */
    @PostMapping("/get-register-captcha")
    public Response<Void> getRegisterCaptcha(@RequestBody GetEmailRequest getEmailRequest) {
        String userEmail = getEmailRequest.getUserEmail();
        // 参数校验
        validateUserEmail(userEmail);
        return emailService.sendRegisterCaptcha(EmailConstant.USER_REGISTER_CAPTCHA_REDIS_KEY, userEmail, EmailSubject.REGISTER_EMAIL);
    }

    /**
     * 发送找回密码邮箱验证码
     */
    @PostMapping("/get-retrieve-password-captcha")
    public Response<Void> getRetrievePasswordCaptcha(@RequestBody GetEmailRequest getEmailRequest) {
        String userEmail = getEmailRequest.getUserEmail();
        // 参数校验
        validateUserEmail(userEmail);
        return emailService.sendRetrievePasswordCaptcha(EmailConstant.USER_RETRIEVE_PASSWORD_CAPTCHA_REDIS_KEY, userEmail, EmailSubject.RETRIEVE_PASSWORD_EMAIL);
    }


    /**
     * 验证邮箱的格式
     */
    private void validateUserEmail(String userEmail) {
        if (StringUtils.isEmpty(userEmail)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱不能为空");
        }
        System.out.println("userEmail:" + userEmail);
        if (!RegUtil.isLegalUserEmail(userEmail)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
        }
    }
}
