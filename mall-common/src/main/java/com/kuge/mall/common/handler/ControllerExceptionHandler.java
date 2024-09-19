package com.kuge.mall.common.handler;

import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.R;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;


/**
 * 全局 Controller 异常处理器
 * created by xbxie on 2024/4/20
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
    /**
     * 处理 Controller 参数验证的异常
     * @param e 参数验证异常
     * @return 失败的 R 实例
     */
    @ExceptionHandler(BindException.class)
    public R<Void> validateExceptionHandler(BindException e) {
        return R.fail(
            e.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining("；"))
        );
    }

    /**
     * 处理自定义异常
     * @param e 自定义异常
     * @return 失败的 R 实例
     */
    @ExceptionHandler(CustomException.class)
    public R<Void> CustomExceptionHandler(CustomException e) {
        return R.fail(e.getCode(), e.getMessage());
    }
}
