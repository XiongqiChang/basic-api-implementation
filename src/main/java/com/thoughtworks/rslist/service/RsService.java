package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: xqc
 * @Date: 2020/9/15 - 09 - 15 - 16:56
 * @Description: com.thoughtworks.rslist.service
 * @version: 1.0
 */
@Service
public class RsService {

    private List<RsEvent> rsList = new ArrayList<>();

    public List<RsEvent> initRsEvent(){
        rsList.add(new RsEvent("第一条事件","无标签"));
        rsList.add(new RsEvent("第二条事件","无标签"));
        rsList.add(new RsEvent("第三条事件","无标签"));
        return  rsList;
    }
}
