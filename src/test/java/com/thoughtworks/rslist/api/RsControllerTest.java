package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private   ObjectMapper objectMapper = new ObjectMapper();

    @Test
    //@Order(1)
    void should_get_rsEvent_all() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无标签")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无标签")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
   // @Order(2)
    void should_get_rsEvent_by_index() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName",is("第一条事件")))
                .andExpect(jsonPath("$.keyWord",is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    //Order(3)
    void should_get_rsEvent_between_start_and_end() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无标签")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无标签")))
                .andExpect(status().isOk());
    }

    @Test
    //@Order(10)
    void should_add_rsEvent() throws Exception {
        RsEvent rsEvent = new RsEvent("小猪佩奇","动画片");

        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/add").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
        //@Order(6)
    void should_update_rsEvent_byId_put() throws Exception {
        RsEvent rsEvent = new RsEvent("猪肉涨价","经济");
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/update/1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName",is("猪肉涨价")))
                .andExpect(jsonPath("$.keyWord",is("经济")))
                .andExpect(status().isOk());

    }


    @Test
    //@Order(7)
    void should_update_rsEvent_keyWord_byId() throws Exception {
        RsEvent rsEvent = new RsEvent(null,"经济");
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/update/2").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName",is("第二条事件")))
                .andExpect(jsonPath("$.keyWord",is("经济")))
                .andExpect(status().isOk());

    }

    @Test
    //@Order(8)
    void should_update_rsEvent_eventName_byId() throws Exception {
        RsEvent rsEvent = new RsEvent("猪肉涨价",null);
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/update/3").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName",is("猪肉涨价")))
                .andExpect(jsonPath("$.keyWord",is("无标签")))
                .andExpect(status().isOk());

    }


    @Test
    //@Order(9)
    void should_delete_rsEvent_byIndex() throws Exception {
        mockMvc.perform(delete("/rs/delete/1")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无标签")))
                .andExpect(jsonPath("$[1].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无标签")))
                .andExpect(status().isOk());
    }


}
