package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
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


   private List<User> userList = new ArrayList<>();

   @DeleteMapping("/user/{index}")
   public ResponseEntity deleteById(@PathVariable int index){
       userRepository.deleteById(index);
       return ResponseEntity.ok("删除成功");
   }

   @GetMapping("/user/{index}")
   public ResponseEntity getUserPOById(@PathVariable  int index){
      Optional<UserPO> userPO = userRepository.findById(index);
      return ResponseEntity.ok(userPO.get());
   }


    @PostMapping("/user/jpa")
    public ResponseEntity addUserByJpa(@RequestBody UserPO user){
        UserPO save = userRepository.save(user);
        if (save != null){
            return ResponseEntity.ok("添加成功");
        }else {
            return  ResponseEntity.badRequest().body("返回失败");
        }

    }


    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody @Valid User user){
        User userByUserName = getUserByUserName(user.getUserName());
        if (userByUserName == null){
            userList.add(user);
        }
        return ResponseEntity.created(null).header("index",String.valueOf(userList.size() -1 )).build();
    }

    @GetMapping("/users")
    public  List<User> listUser(){
        return  userList;
    }

    public User getUserByUserName(String userName){
        Iterator<User> iterator = userList.iterator();
        while (iterator.hasNext()){
            User next = iterator.next();
            if (next.getUserName().equals(userName)) {
                return next;
            }
        }
        return null;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity getUserException(MethodArgumentNotValidException e){
        logger.error("<<<<<<我是一个UserController错误 : invalid user >>>>>>");
        Error error = new Error();
        error.setError("invalid user");
        return  ResponseEntity.badRequest().body(error);
    }
}
