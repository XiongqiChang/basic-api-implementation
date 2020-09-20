package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Author: xqc
 * @Date: 2020/9/18 - 09 - 18 - 14:32
 * @Description: com.thoughtworks.rslist.api
 * @version: 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VoControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RsRepository rsRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private MockMvc mockMvc;


    UserPO userPO;
    RsEventPO rsEventPO;
    @BeforeEach
    void set_up() {
        userRepository.deleteAll();
        rsRepository.deleteAll();
        voteRepository.deleteAll();
        userPO = userRepository.save(UserPO.builder().userName("xqc").age(12).gender("male")
                .email("123@163.com").phone("12345678911").voteNum(10).build());
        rsEventPO = rsRepository.save(RsEventPO.builder().eventName("猪肉涨价")
                .keyWord("经济").user(userPO).voteCountNumber(20).build());


    }

    @Test
    @Order(1)
    void should_get_vote_page() throws Exception {

        for (int i = 0; i< 10; i++){
            voteRepository.save(VotePO.builder().createTime( new Date()).voteCount(i+1)
                    .rsEvent(rsEventPO).user(userPO).build());
        }

        mockMvc.perform(get("/vote/voteRecord").param("userId",String.valueOf(userPO.getId()))
                .param("rsEventId",String.valueOf(rsEventPO.getId())).param("pageIndex","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(jsonPath("$[0].voteCount",is(1)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventPO.getId())))
                .andExpect(jsonPath("$[1].voteCount",is(2)))
                .andExpect(jsonPath("$[2].voteCount",is(3)))
                .andExpect(jsonPath("$[3].voteCount",is(4)))
                .andExpect(jsonPath("$[4].voteCount",is(5)));

        mockMvc.perform(get("/vote/voteRecord").param("userId",String.valueOf(userPO.getId()))
                .param("rsEventId",String.valueOf(rsEventPO.getId())).param("pageIndex","2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(jsonPath("$[0].voteCount",is(6)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId",is(rsEventPO.getId())))
                .andExpect(jsonPath("$[1].voteCount",is(7)))
                .andExpect(jsonPath("$[2].voteCount",is(8)))
                .andExpect(jsonPath("$[3].voteCount",is(9)))
                .andExpect(jsonPath("$[4].voteCount",is(10)));


    }

    @Test
    @Order(2)
    void should_get_vote_record_by_time() throws Exception {
        for (int i = 0; i< 10; i++){
            voteRepository.save(VotePO.builder().createTime(new Date()).voteCount(i+1)
                    .rsEvent(rsEventPO).user(userPO).build());
        }
        mockMvc.perform(get("/vote/records").param("startTime","2020-09-18 17:59")
                .param("endTime", "2020-09-20 18:19").param("pageIndex","1")
                .param("pageSize","2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].voteCount",is(1)))
                .andExpect(jsonPath("$.[1].voteCount",is(2)));
    }

    @Test
    @Order(3)
    void should_get_vote_record_by_id() throws Exception {

        VotePO save = voteRepository.save(VotePO.builder().createTime(new Date()).voteCount( 1)
                .rsEvent(rsEventPO).user(userPO).build());
        mockMvc.perform(get("/vote/"+save.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rsEventId",is(rsEventPO.getId())))
                .andExpect(jsonPath("$.userId",is(userPO.getId())))
                .andExpect(jsonPath("$.voteCount",is(1)));
    }

    @Test
    @Order(4)
    void should_delete_vote_by_id() throws Exception {
        VotePO save = voteRepository.save(VotePO.builder().createTime(new Date()).voteCount( 1)
                .rsEvent(rsEventPO).user(userPO).build());
        mockMvc.perform(delete("/vote/"+save.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/vote/"+save.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    void should_return_bad_request() throws Exception {
        VotePO save = voteRepository.save(VotePO.builder().createTime(new Date()).voteCount( 1)
                .rsEvent(rsEventPO).user(userPO).build());
        mockMvc.perform(delete("/vote/288"))
                .andExpect(status().isBadRequest());
    }

}
