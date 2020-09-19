package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.VoteService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @Author: xqc
 * @Date: 2020/9/15 - 09 - 15 - 17:00
 * @Description: com.thoughtworks.rslist.api
 * @version: 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RsControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private   ObjectMapper objectMapper;

    @Autowired
    private RsRepository rsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
     private VoteService voteService;

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
    void should_add_rsEvent_store_into_database() throws Exception {


        RsEvent rsEvent =new RsEvent("这是一个新的测试","这是一个测试",userPO.getId(),0);
        String buildString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(buildString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<RsEventPO> byUserId = rsRepository.findByUserId(userPO.getId());
        assertEquals("猪肉涨价",byUserId.get(0).getEventName());
        assertEquals("经济",byUserId.get(0).getKeyWord());
        assertEquals("这是一个新的测试",byUserId.get(1).getEventName());
        assertEquals("这是一个测试",byUserId.get(1).getKeyWord());

    }

    @Test
    @Order(2)
    void should_bad_request_return() throws Exception {

        RsEvent build =new RsEvent("猪肉涨价了","经济",2,0);
        String buildString = objectMapper.writeValueAsString(build);
        mockMvc.perform(post("/rs/event").content(buildString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(3)
    void throw_exception_invalid() throws Exception {
        mockMvc.perform(get("/rs/event/" + rsEventPO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName",is("猪肉涨价")))
                .andExpect(jsonPath("$.keyWord",is("经济")))
                .andExpect(jsonPath("$.id",is(rsEventPO.getId())));
    }


    @Test
    @Order(4)
    void  should_update_rsEvent() throws Exception {

        RsEvent rsEvent = new RsEvent("特别被本更高更新的测试例子","最新的",userPO.getId(),0);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/"+ rsEventPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Optional<RsEventPO> byId = rsRepository.findById(rsEventPO.getId());
        assertEquals("特别被本更高更新的测试例子",byId.get().getEventName());
    }


    @Test
    @Order(5)
    void  should_update__user_id_notMatch_rsEvent() throws Exception {

        RsEvent rsEvent = new RsEvent("猪肉又涨价了","最新经济",9,0);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/"+ rsEventPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(6)
    void  should_update__rsEvent() throws Exception {

        RsEvent rsEvent = new RsEvent("猪肉又涨价了","最新经济",userPO.getId(),0);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/"+ rsEventPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/event/"+rsEventPO.getId()))
                .andExpect(jsonPath("$.eventName",is("猪肉又涨价了")))
                .andExpect(jsonPath("$.keyWord",is("最新经济")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    void  should_throw_exception_update__rsEvent() throws Exception {

        RsEvent rsEvent = new RsEvent("猪肉又涨价了","最新经济",userPO.getId(),0);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/-1"+ rsEventPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invalid param")));

    }

    @Test
    @Order(8)
    void  should_update_rsEvent_eventName_isEmpty() throws Exception {

        RsEvent rsEvent = new RsEvent("","特别新的关键字",userPO.getId(),0);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/"+rsEventPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/event/"+rsEventPO.getId()))
                .andExpect(jsonPath("$.eventName",is("猪肉涨价")))
                .andExpect(jsonPath("$.keyWord",is("特别新的关键字")))
                .andExpect(jsonPath("$.id",is(rsEventPO.getId())))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    void  should_update_rsEvent_keyWord_isEmpty() throws Exception {
        RsEvent rsEvent = new RsEvent("猪肉","",userPO.getId(),0);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/"+rsEventPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/event/"+rsEventPO.getId()))
                .andExpect(jsonPath("$.eventName",is("猪肉")))
                .andExpect(jsonPath("$.keyWord",is("经济")))
                .andExpect(jsonPath("$.id",is(rsEventPO.getId())))
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    void  should_vote_rsEvent() throws Exception {

        Vote vote = new Vote(4,new Date(),userPO.getId(),rsEventPO.getId());
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/" + rsEventPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<Vote> voteRecordByUserIdAndByRsId = voteService.getVoteRecordByUserIdAndByRsId(userPO.getId(), rsEventPO.getId(),1);
         Integer voteCount = voteRecordByUserIdAndByRsId.get(0).getVoteCount();
         Integer userId = voteRecordByUserIdAndByRsId.get(0).getUserId();
         Integer rsEventId = voteRecordByUserIdAndByRsId.get(0).getRsEventId();
         assertEquals(4,voteCount);
         assertEquals(userPO.getId(),userId);
         assertEquals(rsEventPO.getId(),rsEventId);
    }


    @Test
    @Order(11)
    void  should_vote_rsEvent_out_of_vote_num() throws Exception {

        Vote vote = new Vote(15,new Date(),userPO.getId(),rsEventPO.getId());
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/" + rsEventPO.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(12)
    void  should_vote_rsEvent_rsEvent_not_exist() throws Exception {

        Vote vote = new Vote(1,new Date(),userPO.getId(),1);
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(13)
    void  should_throw_exception_when_rsId_not_match() throws Exception {

        Vote vote = new Vote(5,new Date(),userPO.getId(),rsEventPO.getId());
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/-1" ).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invalid param")));

    }

    @Test
    @Order(14)
    void should_get_rsEvent_all() throws Exception {
        RsEvent rsEvent =new RsEvent("测试新加的热搜","测试",userPO.getId(),0);
        String buildString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(buildString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/event"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("猪肉涨价")))
                .andExpect(jsonPath("$[0].keyWord",is("经济")))
                .andExpect(jsonPath("$[1].eventName",is("测试新加的热搜")))
                .andExpect(jsonPath("$[1].keyWord",is("测试")));

    }

    @Test
    @Order(15)
    void should_get_rsEvent_by_id() throws Exception {
        mockMvc.perform(get("/rs/event/" + rsEventPO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName",is("猪肉涨价")))
                .andExpect(jsonPath("$.keyWord",is("经济")))
                .andExpect(jsonPath("$.voteCountNumber",is(20)));

    }

    @Test
    @Order(16)
    void should_throw_exception() throws Exception {
        mockMvc.perform(get("/rs/event/-1"))
                .andExpect(jsonPath("$.error",is("invalid param")));
    }

    @Test
    @Order(17)
    void  should_delete_by_id() throws Exception {
        mockMvc.perform(delete("/rs/event/"+rsEventPO.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/event/" + rsEventPO.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(18)
    void  should_throw_delete_by_id_invalid() throws Exception {
        mockMvc.perform(delete("/rs/event/-2"))
                .andExpect(status().isBadRequest());

    }


    @Test
    @Order(19)
    void  should__throw_exception_by_id_invalid() throws Exception {
        mockMvc.perform(delete("/rs/event/-2"))
                .andExpect(jsonPath("$.error",is("invalid param")));

    }




}
