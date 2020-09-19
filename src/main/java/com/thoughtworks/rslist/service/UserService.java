package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: xqc
 * @Date: 2020/9/18 - 09 - 18 - 19:30
 * @Description: com.thoughtworks.rslist.service
 * @version: 1.0
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean updateUserPOById(Integer id, User user){
        if (!userRepository.findById(id).isPresent()){
            return  false;
        }else {
            UserPO userPo = userRepository.findById(id).get();
            BeanUtils.copyProperties(user,userPo);
            userRepository.save(userPo);
            return true;
        }
    }


    public boolean addUser(User user){

        if (userRepository.findUserPOByUserName(user.getUserName()) != null){
            return false;
        }
        UserPO userPO = UserPO.builder().userName(user.getUserName())
                .age(user.getAge())
                .phone(user.getPhone())
                .email(user.getEmail())
                .gender(user.getGender())
                .voteNum(user.getVoteNum()).build();
        userRepository.save(userPO);
        return true;
    }


    public void deleteById(Integer id){
        userRepository.deleteById(id);
    }


    public User findById(Integer id){

        Optional<UserPO> byId = userRepository.findById(id);
        if (!byId.isPresent()){
            return  null;
        }
        UserPO userPO = byId.get();
        User userBuilder = User.builder().userName(userPO.getUserName()).age(userPO.getAge())
                .email(userPO.getEmail()).gender(userPO.getGender())
                .phone(userPO.getPhone()).voteNum(userPO.getVoteNum()).build();
        return  userBuilder;

    }


    public List<User> getUserLists(){
        List<UserPO> all = userRepository.findAll();
       List<User> userList =  all.stream().map(item -> User.builder().userName(item.getUserName()).voteNum(item.getVoteNum())
                .phone(item.getPhone()).email(item.getEmail()).gender(item.getGender()).age(item.getAge()).build()
        ).collect(Collectors.toList());
       return userList;
    }

}
