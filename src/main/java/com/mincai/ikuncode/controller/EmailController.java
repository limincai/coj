package com.mincai.ikuncode.controller;

import com.mincai.ikuncode.common.Response;
import com.mincai.ikuncode.constant.EmailConstant;
import com.mincai.ikuncode.constant.EmailSubject;
import com.mincai.ikuncode.enums.ErrorCode;
import com.mincai.ikuncode.exception.BusinessException;
import com.mincai.ikuncode.model.dto.UserDTO;
import com.mincai.ikuncode.service.EmailService;
import com.mincai.ikuncode.utils.RegUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
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
     * 发送邮箱验证码
     */
    @PostMapping("/send-register-captcha")
    public Response<Void> sendRegisterCaptcha(UserDTO userDTO) {
        // 参数校验
        String userEmail = userDTO.getUserEmail();
        if (StringUtils.isEmpty(userEmail)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱不能为空");
        }
        if (!RegUtil.isLegalUserEmail(userEmail)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
        }
        return emailService.sendCaptcha(EmailConstant.USER_REGISTER_CAPTCHA_REDIS_KEY, userEmail, EmailSubject.REGISTER_EMAIL);
    }

}
