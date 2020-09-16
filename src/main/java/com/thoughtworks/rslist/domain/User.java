package com.thoughtworks.rslist.domain;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * @Author: xqc
 * @Date: 2020/9/16 - 09 - 16 - 11:09
 * @Description: com.thoughtworks.rslist.domain
 * @version: 1.0
 */
@Data
public class User {

    @NotNull
    @Size(max = 8)
    private  String  userName;

    @NotNull
    @Min(18)
    @Max(100)
    private  Integer age;

    @NotNull
    private String gender;

    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "1\\d{10}")
    private String phone;

    public User(@NotNull @Size(max = 8) String userName, @NotNull @Min(18) @Max(100) Integer age, @NotNull String gender, @Email String email, @NotNull @Pattern(regexp = "1\\d{10}") String phone) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    public User() {
    }
}
