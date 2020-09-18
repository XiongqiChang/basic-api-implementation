package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author: xqc
 * @Date: 2020/9/16 - 09 - 16 - 11:40
 * @Description: com.thoughtworks.rslist.api
 * @version: 1.0
 */
@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;



   @DeleteMapping("/user/{id}")
   public ResponseEntity deleteById(@PathVariable int id){
       if (userRepository.findById(id) != null){
           userRepository.deleteById(id);
           return ResponseEntity.ok("删除成功");
       }else {
           return ResponseEntity.badRequest().build();
       }
   }

   @GetMapping("/user/{index}")
   public ResponseEntity getUserPOById(@PathVariable  int index){
      Optional<UserPO> userPO = userRepository.findById(index);
      if (userPO.isPresent()){
          UserVO user = new UserVO();
          BeanUtils.copyProperties(userPO.get(),user);
          return ResponseEntity.ok().body(user);
      }else {
          return ResponseEntity.notFound().build();
      }

   }

   @GetMapping("/user")
   public ResponseEntity getUserAll(){
       List<UserPO> all = userRepository.findAll();
       List<UserVO> responseUser = new ArrayList<>();
       for (UserPO userPO : all){
           UserVO user = new UserVO();
           BeanUtils.copyProperties(userPO,user);
           responseUser.add(user);
       }
       return  ResponseEntity.ok().body(responseUser);
   }



    @PostMapping("/user")
    public ResponseEntity addUserByJpa(@RequestBody User user){

        if (userRepository.findUserPOByUserName(user.getUserName()) != null){
            return ResponseEntity.badRequest().build();
        }else{
            UserPO userPO = UserPO.builder().userName(user.getUserName())
                    .age(user.getAge())
                    .gender(user.getGender())
                    .age(user.getAge())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .voteNum(user.getVoteNum())
                    .build();
            userRepository.save(userPO);
            return ResponseEntity.ok("添加成功");
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity updateUserById(@PathVariable int id,@RequestBody UserVO userVO){
        Optional<UserPO> byIdUser = userRepository.findById(id);
        if (byIdUser.isPresent()){
            UserPO userPO = byIdUser.get();
            BeanUtils.copyProperties(userVO,userPO);
            userRepository.save(userPO);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity getUserException(MethodArgumentNotValidException e){
        logger.error("<<<<<<我是一个UserController错误 : invalid user >>>>>>");
        Error error = new Error();
        error.setError("invalid user");
        return  ResponseEntity.badRequest().body(error);
    }
}
