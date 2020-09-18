package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * @Author: xqc
 * @Date: 2020/9/16 - 09 - 16 - 11:09
 * @Description: com.thoughtworks.rslist.domain
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @NotNull
    @Size(max = 8,message = "年龄不能超过限制")
    @JsonProperty(value = "user_name")
    private  String  userName;

    @NotNull
    @Min(18)
    @Max(100)
    @JsonProperty(value = "user_age")
    private  Integer age;

    @NotNull
    @JsonProperty(value = "user_gender")
    private String gender;

    @Email
    @JsonProperty(value = "user_email")
    private String email;

    @NotNull
    @Pattern(regexp = "1\\d{10}")
    @JsonProperty(value = "user_phone")
    private String phone;

    @NotNull
    private Integer voteNum;



}
