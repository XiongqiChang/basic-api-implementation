package com.thoughtworks.rslist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * @Author: xqc
 * @Date: 2020/9/17 - 09 - 17 - 22:36
 * @Description: com.thoughtworks.rslist.domain
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@Builder
public class Vote {

    @NotNull
    private  Integer voteCount;

    @NotNull
    private Date createTime;

    @NotNull
    private Integer userId;
}
