package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {


   private List<RsEvent> rsEventList = initRsEvent();
  private  List<RsEvent> initRsEvent(){
   List<RsEvent> rsList = new ArrayList<>();
    rsList.add(new RsEvent("第一条事件","无标签"));
    rsList.add(new RsEvent("第二条事件","无标签"));
    rsList.add(new RsEvent("第三条事件","无标签"));
    return  rsList;
  }

  @GetMapping("/rs/list")
  public  List<RsEvent> getRsEventList(@RequestParam(value = "start",required = false)Integer start,
                                       @RequestParam(value = "end",required = false)Integer end){
    if (start == null || end ==  null){
      return rsEventList;
    }
    //subList这个方法是左闭右开的
    return  rsEventList.subList(start-1,end);
  }


  @GetMapping("/rs/{index}")
  public RsEvent getRsEventByIndex(@PathVariable(value = "index") Integer id){
    return rsEventList.get(id - 1);
  }


    @PostMapping("/rs/add")
    public void addRsEvent(@RequestBody RsEvent rsEvent){

      rsEventList.add(rsEvent);

    }





}
