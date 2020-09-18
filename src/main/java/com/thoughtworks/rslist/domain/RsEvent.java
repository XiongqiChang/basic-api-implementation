package com.thoughtworks.rslist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author: xqc
 * @Date: 2020/9/15 - 09 - 15 - 16:55
 * @Description: com.thoughtworks.rslist.domain
 * @version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RsEvent {

       private int id;

    @NotNull
    private String eventName;

    @NotNull
    private String keyWord;

    @NotNull
    private Integer  userId;

    private Integer voteCountNumber;

    public RsEvent(String eventName,String keyWord,Integer userId){
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.userId = userId;
    }



}
