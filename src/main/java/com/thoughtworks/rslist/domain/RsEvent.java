package com.thoughtworks.rslist.domain;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author: xqc
 * @Date: 2020/9/15 - 09 - 15 - 16:55
 * @Description: com.thoughtworks.rslist.domain
 * @version: 1.0
 */
@Data
public class RsEvent {

    @NotNull
    private String eventName;

    @NotNull
    private String keyWord;

    @NotNull
    @Valid
    private User user;

    public RsEvent(String eventName, String keyWord, User user) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.user = user;
    }

    public RsEvent(String eventName, String keyWord){
        this.eventName = eventName;
        this.keyWord = keyWord;
    }

    public RsEvent(){

    }
}
