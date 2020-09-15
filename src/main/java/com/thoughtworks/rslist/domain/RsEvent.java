package com.thoughtworks.rslist.domain;

import lombok.Data;

/**
 * @Author: xqc
 * @Date: 2020/9/15 - 09 - 15 - 16:55
 * @Description: com.thoughtworks.rslist.domain
 * @version: 1.0
 */
@Data
public class RsEvent {
    private String eventName;
    private String keyWord;

    public RsEvent(String eventName, String keyWord) {
        this.eventName = eventName;
        this.keyWord = keyWord;
    }

    public RsEvent(){

    }
}
