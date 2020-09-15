package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {


   List<RsEvent> rsEventList = initRsEvent();
  private  List<RsEvent> initRsEvent(){
   List<RsEvent> rsList = new ArrayList<>();
    rsList.add(new RsEvent("第一条事件","无标签"));
    rsList.add(new RsEvent("第二条事件","无标签"));
    rsList.add(new RsEvent("第三条事件","无标签"));
    return  rsList;
  }

  @GetMapping("/rs/list")
  public  List<RsEvent> getRsEventList(){
     return  rsEventList;
  }


  @GetMapping("/rs/{index}")
  public RsEvent getRsEventByIndex(@PathVariable(value = "index") Integer id){
    return rsEventList.get(id - 1);
  }






}
