package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.service.VoteService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserService userService;

    @Autowired
    private VoteRepository voteRepository;

    UserPO userPO;
    RsEventPO rsEventPO;
    VotePO votePO;

    @BeforeEach
    void set_up(){
        userRepository.deleteAll();
        rsRepository.deleteAll();
        voteRepository.deleteAll();
        userPO = userRepository.save(UserPO.builder().userName("xqc").age(12).gender("male")
                .email("123@163.com").phone("12345678911").voteNum(10).build());
        rsEventPO = rsRepository.save(RsEventPO.builder().eventName("猪肉涨价")
                .keyWord("经济").user(userPO).voteCountNumber(20).build());
        votePO = VotePO.builder().createTime(new Date()).voteCount(1)
                .rsEvent(rsEventPO).user(userPO).build();
        voteRepository.save(votePO);

    }

    @Test
    @Order(1)
    void should_delete_user_and_rsList_and_vote() throws Exception {
        mockMvc.perform(delete("/user/"+userPO.getId()))
                .andExpect(status().isOk());
        User byId = userService.findById(userPO.getId());
       List<Vote> voteListByUserId = voteService.getVoteListByUserId(userPO.getId());
        assertEquals(null,byId);
        assertEquals(0,voteListByUserId.size());
    }
    @Test
    @Order(2)
    void should_delete_user_and_rsList_and_vote_id_not_match() throws Exception{
        mockMvc.perform(delete("/user/12"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    void should_add_user() throws Exception {
        User build = new User("zmtxq",12,"female","12@163.com","12345678911",10);
        String string = objectMapper.writeValueAsString(build);
        mockMvc.perform(post("/user").content(string).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        UserPO userPo = userRepository.findUserPOByUserName("zmtxq");
        assertEquals("zmtxq",userPo.getUserName());
        assertEquals("female",userPo.getGender());
        assertEquals("12@163.com",userPo.getEmail());
        assertEquals("12345678911",userPo.getPhone());
    }

    @Test
    @Order(4)
    void should_add_user_has_exist() throws Exception {
        User build = new User("xqc",12,"femao","12@163.com","12345678911",10);
        String string = objectMapper.writeValueAsString(build);
        mockMvc.perform(post("/user").content(string).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    void should_get_user_by_id() throws Exception {
        mockMvc.perform(get("/user/" + userPO.getId()))
                .andExpect(jsonPath("$.user_name",is("xqc")))
                .andExpect(jsonPath("$.user_age",is(12)))
                .andExpect(jsonPath("$.user_gender",is("male")))
                .andExpect(jsonPath("$.user_email",is("123@163.com")))
                .andExpect(jsonPath("$.user_phone",is("12345678911")))
                .andExpect(status().isOk());
    }


    @Test
    @Order(6)
    void should_bad_request_user_by_id() throws Exception {
        mockMvc.perform(get("/user/-2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    void should_get_userList() throws Exception {
        User build = new User("zmt",12,"female","12@163.com","12345678911",10);
        String string = objectMapper.writeValueAsString(build);
        mockMvc.perform(post("/user").content(string).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$.[0].user_name",is("xqc")))
                .andExpect(jsonPath("$.[0].user_age",is(12)))
                .andExpect(jsonPath("$.[0].user_email",is("123@163.com")))
                .andExpect(jsonPath("$.[1].user_age",is(12)))
                .andExpect(jsonPath("$.[1].user_name",is("zmt")))
                .andExpect(jsonPath("$.[1].user_email",is("12@163.com")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    void should_update_user_by_id() throws Exception {
        User newUser = new User("zmt",12,"female","12@163.com","12345678911",10);
        String jsonString = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(put("/user/"+userPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user/" + userPO.getId()))
                .andExpect(jsonPath("$.user_name",is("zmt")))
                .andExpect(jsonPath("$.user_age",is(12)))
                .andExpect(jsonPath("$.user_gender",is("female")))
                .andExpect(jsonPath("$.user_email",is("12@163.com")))
                .andExpect(jsonPath("$.user_phone",is("12345678911")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    void should_update_user_by_id_not_match() throws Exception {
        User newUser = new User("zmt", 12, "female", "12@163.com", "12345678911", 10);
        String jsonString = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(put("/user/122").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
