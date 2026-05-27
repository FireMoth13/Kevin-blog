package com.kevin.blog.exception;

import com.kevin.blog.common.result.ResultCode;
import com.kevin.blog.common.exception.BusinessException;
import com.kevin.blog.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //拦截BusinessException，返回Result对象
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e){
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return Result.fail(e.getCode(),e.getMessage());
    }

    //拦截参数校验异常，后面用到@Valid注解时会触发
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e){
        String msg=e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                .orElse("参数错误");
        return Result.fail(ResultCode.BAD_REQUEST, msg);
    }

    //拦截其他未处理的异常，返回服务器错误
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e){
        log.error("服务器异常", e);
        return Result.fail(ResultCode.INTERNAL_ERROR,"服务器发生异常，请稍后再试");
    }
}
