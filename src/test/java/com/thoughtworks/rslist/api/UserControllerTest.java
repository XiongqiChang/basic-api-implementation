package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsRepository;
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
    @Autowired
    private RsRepository rsRepository;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    @Order(1)
    void should_delete_user_and_rsList() throws Exception {
        mockMvc.perform(delete("/user/1")).andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void should_add_user() throws Exception {
        UserPO build = UserPO.builder().userName("xqc").phone("18888888888").gender("male").email("a@163.com").age(18).build();
        String string = objectMapper.writeValueAsString(build);
        mockMvc.perform(post("/user").content(string).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void should_delete_user() throws Exception {
        UserPO build = UserPO.builder().userName("xqc").phone("18888888888").gender("male").email("a@163.com").age(18).build();
        UserPO save = userRepository.save(build);
        RsEventPO rsEventPO = RsEventPO.builder().keyWord("经济").eventName("张三").userPO(save).build();
        String string = objectMapper.writeValueAsString(build);
        mockMvc.perform(delete("/user/{id}",save.getId()).content(string).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
