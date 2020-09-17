package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.HeaderResultMatchers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    @Test
    @Order(1)
    void should_add_rsEvent_store_into_database() throws Exception {

       /* UserPO build1 = UserPO.builder().userName("zmt").age(18).email("xqc@163.com").phone("12345678911").gender("femal").build();
        UserPO save = userRepository.save(build1);*/
        RsEvent rsEvent =new RsEvent("这是一个新的测试","这是一个测试",15);
        String buildString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(buildString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
     /*   List<RsEventPO> all = rsRepository.findAll();
        assertEquals("测试",all.get(1).getEventName());
        assertEquals("这是一个测试",all.get(1).getKeyWord());
        assertEquals(save.getId(),all.get(1).getUserPO().getId());*/
    }

    @Test
    @Order(2)
    void should_bad_request_return() throws Exception {
       /* UserPO build1 = UserPO.builder().userName("zmt").age(18).email("xqc@163.com").phone("12345678911").gender("femal").build();
        userRepository.save(build1);*/
        RsEvent build =new RsEvent("猪肉涨价了","经济",2);
        String buildString = objectMapper.writeValueAsString(build);
        mockMvc.perform(post("/rs/event").content(buildString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }




    @Test
    @Order(3)
    void  should_update_rsEvent() throws Exception {

        RsEvent rsEvent = new RsEvent("更高更新的测试例子","最新的",15);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/24").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Optional<RsEventPO> byId = rsRepository.findById(24);
        assertEquals("更高更新的测试例子",byId.get().getEventName());
    }



    @Test
    @Order(4)
    void  should_update__userid_notMatch_rsEvent() throws Exception {

        RsEvent rsEvent = new RsEvent("","最新新的关键字",9);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/12").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(5)
    void  should_update_rsEvent_eventName_or_keyWord_isEmpty() throws Exception {

        RsEvent rsEvent = new RsEvent("","特别新的关键字",11);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/12").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Optional<RsEventPO> byId = rsRepository.findById(12);
        assertEquals("最新的热搜",byId.get().getEventName());
    }


    @Test
    @Order(6)
    void  should_vote_rsEvent() throws Exception {

        Vote vote = new Vote(4,new Date(),15);
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/16").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }



}
