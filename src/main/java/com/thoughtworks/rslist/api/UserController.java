package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: xqc
 * @Date: 2020/9/16 - 09 - 16 - 11:40
 * @Description: com.thoughtworks.rslist.api
 * @version: 1.0
 */
@RestController
public class UserController {

    private List<User> userList = new ArrayList<>();

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
        Error error = new Error();
        error.setError("invalid user");
        return  ResponseEntity.badRequest().body(error);
    }
}
