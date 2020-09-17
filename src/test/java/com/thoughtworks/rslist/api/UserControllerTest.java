package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @Author: xqc
 * @Date: 2020/9/16 - 09 - 16 - 11:41
 * @Description: com.thoughtworks.rslist.api
 * @version: 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    void should_add_user() throws Exception {
        User user = new User("xqc",18,"male","a@163.com","18888888888");
        User user2 = new User("zmt",18,"male","a@163.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJsonString = objectMapper.writeValueAsString(user);
        String userJsonString2 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/user").content(userJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index","0"));

        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].user_name",is("xqc")))
                .andExpect(jsonPath("$[0].user_age",is(18)))
                .andExpect(jsonPath("$[0].user_gender",is("male")))
                .andExpect(jsonPath("$[0].user_email",is("a@163.com")))
                .andExpect(jsonPath("$[0].user_phone",is("18888888888")))
                .andExpect(status().isOk());
        mockMvc.perform(post("/user").content(userJsonString2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(header().string("index","1"));
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[1].user_name",is("zmt")))
                .andExpect(jsonPath("$[1].user_age",is(18)))
                .andExpect(jsonPath("$[1].user_gender",is("male")))
                .andExpect(jsonPath("$[1].user_email",is("a@163.com")))
                .andExpect(jsonPath("$[1].user_phone",is("18888888888")))
                .andExpect(status().isOk());
        mockMvc.perform(post("/user").content(userJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(header().string("index","1"));
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @Test
    @Order(2)
    void should_add_user_username_super_limit() throws Exception {
        User user = new User("xqcxqcxqcxqcxqc",18,"male","a@163.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    void should_add_user_age_super_limit() throws Exception {
        User user = new User("xia",118,"male","a@163.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    void should_add_user_email_not_stand() throws Exception {
        User user = new User("xia",18,"male","a163.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    void should_add_user_phone_not_stand() throws Exception {
        User user = new User("xia",18,"male","a@163.com","08888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    @Order(6)
    void should_throw_user_invalid_exception() throws Exception {
        User user = new User("xia",18,"male","a@163.com","08888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    @Order(7)
    @Transactional
    void should_add_user_into_database(){
        UserPO user = new UserPO("xqcc",18,"male","a@163.com","18888888889");
        UserPO save = userRepository.save(user);
        assertEquals(user,save);
    }

    @Test
    @Order(8)
    void should_delete_user_into_database_by_id(){
        //UserPO user = new UserPO("xia",18,"male","a@163.com","18888888889");
        userRepository.deleteById(2);
        boolean b = userRepository.existsById(2);
        assertEquals(false,b);
    }

    @Test
    @Order(9)
    void should_get_user_into_database_by_id(){
        Optional<UserPO> byId = userRepository.findById(2);
        assertNotNull(byId);

    }



}
