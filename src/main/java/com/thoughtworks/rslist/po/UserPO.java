package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @Author: xqc
 * @Date: 2020/9/17 - 09 - 17 - 12:18
 * @Description: com.thoughtworks.rslist.po
 * @version: 1.0
 */
@Data
@Table(name = "user")
@Entity
@Builder
@AllArgsConstructor
public class UserPO {

    @GeneratedValue
    @Id
    private int id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "age")
    private Integer  age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "vote_num")
    private Integer voteNum;

     public  UserPO(){}

     @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
     private List<RsEventPO> rsEventPOS;


    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private List<VotePO> votePOS;

}
