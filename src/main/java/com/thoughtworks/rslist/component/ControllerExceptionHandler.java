package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * @Author: xqc
 * @Date: 2020/9/16 - 09 - 16 - 18:21
 * @Description: com.thoughtworks.rslist.component
 * @version: 1.0
 */
@ControllerAdvice
public class ControllerExceptionHandler {


    private Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity rsExceptionHandler(Exception e){
        String errorMessage = "";
        if (e instanceof MethodArgumentNotValidException){
            errorMessage = "invalid param";
            logger.error("我是一个RsController参数错误  " + errorMessage);
        }else {
            errorMessage = e.getMessage();
            logger.error("我是一个RsController运行错误  " + errorMessage);
        }
        Error error = new Error();
        error.setError(errorMessage);
        return  ResponseEntity.badRequest().body(error);
    }

}
