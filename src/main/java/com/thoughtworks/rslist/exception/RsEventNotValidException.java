package com.thoughtworks.rslist.exception;

/**
 * @Author: xqc
 * @Date: 2020/9/16 - 09 - 16 - 17:57
 * @Description: com.thoughtworks.rslist.exception
 * @version: 1.0
 */
public class RsEventNotValidException extends  RuntimeException {

    private  String message;

    public RsEventNotValidException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
