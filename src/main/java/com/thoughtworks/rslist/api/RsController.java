package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {

    @Autowired
    private  UserController userController;

    private List<RsEvent> rsEventList = initRsEvent();
    private  List<RsEvent> initRsEvent(){
    List<RsEvent> rsList = new ArrayList<>();
    User user = new User("xqc",18,"male","a@163.com","18888888888");
    rsList.add(new RsEvent("第一条事件","无标签",user));
    rsList.add(new RsEvent("第二条事件","无标签",user));
    rsList.add(new RsEvent("第三条事件","无标签",user));
    return  rsList;
  }


  @GetMapping("/rs/list")
  public  List<RsEvent> getRsEventList(@RequestParam(value = "start",required = false)Integer start,
                                       @RequestParam(value = "end",required = false)Integer end){
    if (start == null || end ==  null){
      return rsEventList;
    }
    return  rsEventList.subList(start-1,end);

  }


   @GetMapping("/rs/{index}")
   public RsEvent getRsEventByIndex(@PathVariable(value = "index") Integer id){
        if (id < 0 || id > rsEventList.size()){
            throw new RuntimeException();
        }
        return rsEventList.get(id - 1);
   }


   @PostMapping("/rs")
   public void addRsEvent(@RequestBody @Validated RsEvent rsEvent){

       String userName = rsEvent.getUser().getUserName();
       User user = userController.getUserByUserName(userName);
       if (user != null){
           rsEvent.setUser(user);
           rsEventList.add(rsEvent);
       }else {
           userController.addUser(rsEvent.getUser());
           rsEventList.add(rsEvent);
       }
    }


    @PutMapping("/rs/update/{index}")
    public void updateRsEvent(@PathVariable Integer index,
                              @RequestBody @Valid RsEvent rsEvent){

        RsEvent rsEvent1 = rsEventList.get(index - 1);
        String eventName = rsEvent.getEventName();
        String keyWord = rsEvent.getKeyWord();
        if (eventName != ""){
            rsEvent1.setEventName(eventName);
        }
        if (keyWord != ""){
            rsEvent1.setKeyWord(keyWord);
        }
    }

    @DeleteMapping("/rs/delete/{index}")
    public void deleteRsEvent(@PathVariable Integer index){
        if (index < 0 || index > rsEventList.size()){
            throw new RuntimeException();
        }

        RsEvent rsEvent = rsEventList.get(index);
        if (rsEvent == null){
            return;
        }else{
            rsEventList.remove(index - 1);
        }
    }
}
