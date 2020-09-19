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

    @NotNull
    private String eventName;

    @NotNull
    private String keyWord;

    @NotNull
    private Integer  userId;

    @NotNull
    private Integer voteCountNumber;





}
