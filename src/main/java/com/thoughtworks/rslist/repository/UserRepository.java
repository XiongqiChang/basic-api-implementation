package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.UserPO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @Author: xqc
 * @Date: 2020/9/17 - 09 - 17 - 12:23
 * @Description: com.thoughtworks.rslist.repository
 * @version: 1.0
 */
public interface UserRepository  extends CrudRepository<UserPO,Integer> {


    UserPO  findUserPOByUserName(String userName);


    @Override
    List<UserPO> findAll();
}
