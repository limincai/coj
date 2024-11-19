package com.mincai.coj.exception;

import com.mincai.coj.common.Response;
import com.mincai.coj.common.Result;
import com.mincai.coj.enums.ErrorCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 文件上传异常处理类
 *
 * @author limincai
 */
@RestControllerAdvice
public class FileUploadExceptionAdvice {

    /**
     * 文件最大上传异常处理
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Response<Void> handleMaxSizeException() {
        return Result.error(ErrorCode.MAX_SIZE_ERROR, "上传文件过大，请上传 5MB 以下的文件");
    }
}
