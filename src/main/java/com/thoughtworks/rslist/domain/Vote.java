package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public class Vote {

    @NotNull
    private  Integer voteCount;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer rsEventId;

}
