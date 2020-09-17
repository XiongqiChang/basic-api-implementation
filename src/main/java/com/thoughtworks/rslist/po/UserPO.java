package com.thoughtworks.rslist.po;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: xqc
 * @Date: 2020/9/17 - 09 - 17 - 12:18
 * @Description: com.thoughtworks.rslist.po
 * @version: 1.0
 */
@Data
@Table(name = "user")
@Entity
public class UserPO {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Column(name = "username")
    private String userName;

    @Column(name = "age")
    private Integer  age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    public  UserPO(){}

    public UserPO( String userName, Integer age, String gender, String email, String phone) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }
}
