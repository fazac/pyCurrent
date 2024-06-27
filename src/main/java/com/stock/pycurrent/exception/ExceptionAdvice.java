package com.stock.pycurrent.exception;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author fzc
 * @date 2023/6/29 14:53
 * @description
 */
@CommonsLog
@RestControllerAdvice
public class ExceptionAdvice {
    @ResponseBody//将异常的返回格式为json格式
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)//处理哪种异常
    public String exceptionHandler(Exception e) {
        log.error("执行异常", e);//将异常打印在控制台
        if (e instanceof MyException exception) {
            return exception.getMsg();
        } else {
            return "后端执行异常";
        }
    }

}
