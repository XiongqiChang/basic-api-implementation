package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

/**
 * @Author: xqc
 * @Date: 2020/9/16 - 09 - 16 - 18:21
 * @Description: com.thoughtworks.rslist.component
 * @version: 1.0
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class, BindException.class })
    public ResponseEntity rsExceptionHandler(Exception e){
        String errorMessage = "";
        if (e instanceof MethodArgumentNotValidException){
            errorMessage = "invalid param";
        } else if (e instanceof BindException){
            errorMessage = "invalid user";
        }else  {
            errorMessage = e.getMessage();
        }
        Error error = new Error();
        error.setError(errorMessage);
        return  ResponseEntity.badRequest().body(error);
    }

}
