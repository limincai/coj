package com.mincai.coj.utils;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 *
 * @author limincai
 */
public class RegUtil {

    /**
     * 判断是否为合法的用户名
     * 账号为 8 - 16 位不允许带特殊字符
     */
    public static boolean isLegalUserAccount(String userAccount) {
        String regex = "^[a-zA-Z0-9]{8,16}$";
        return Pattern.matches(regex, userAccount);
    }

    /**
     * 判断是否为合法的用户密码
     * 密码为 8 - 16 位不允许带特殊字符
     */
    public static boolean isLegalUserPassword(String userPassword) {
        String regex = "^[a-zA-Z0-9]{8,16}$";
        return Pattern.matches(regex, userPassword);
    }

    /**
     * 判断是否为合法的邮箱
     */
    public static boolean isLegalUserEmail(String userEmail) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, userEmail);
    }

    /**
     * 判断是否为合法的验证码
     */
    public static boolean isLegalCaptcha(String verificationCode) {
        String regex = "^[A-Za-z0-9]{5}$";
        return Pattern.matches(regex, verificationCode);
    }

    /**
     * 判断是否为合法的图片格式
     */
    public static boolean isLegalPictureFormat(String verificationCode) {
        String regex = "(?i)^.*\\.(jpg|jpeg|png|gif|bmp|tiff|webp)$";
        return Pattern.matches(regex, verificationCode);
    }
}
