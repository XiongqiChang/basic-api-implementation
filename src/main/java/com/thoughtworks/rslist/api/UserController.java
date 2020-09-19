package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private UserService userService;



   @DeleteMapping("/user/{id}")
   public ResponseEntity deleteById(@PathVariable int id){
       if (userService.findById(id) != null){
           userService.deleteById(id);
           return ResponseEntity.ok("删除成功");
       }else {
           return ResponseEntity.badRequest().build();
       }
   }

   @GetMapping("/user/{id}")
   public ResponseEntity getUserPOById(@PathVariable  Integer id){
     if (id <= 0){
         return ResponseEntity.badRequest().build();
     }
       User byId = userService.findById(id);
       return ResponseEntity.ok(byId);

   }

   @GetMapping("/user")
   public ResponseEntity getUserAll(){
       List<User> userLists = userService.getUserLists();
       return  ResponseEntity.ok().body(userLists);
   }

    @PostMapping("/user")
    public ResponseEntity addUserByJpa(@RequestBody User user){

        if (!userService.addUser(user)){
            return ResponseEntity.badRequest().build();
        }else {
            return ResponseEntity.ok().build();
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity updateUserById(@PathVariable int id,@RequestBody User user){

        if (!userService.updateUserPOById(id,user)){
            return ResponseEntity.notFound().build();
        }else{
           return ResponseEntity.ok().build();
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
