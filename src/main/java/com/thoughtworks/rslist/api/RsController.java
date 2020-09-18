package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.vo.RsEventVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {

    @Autowired
    private RsRepository rsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;


    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent){

        Optional<UserPO> byId = userRepository.findById(rsEvent.getUserId());
        if (byId.isPresent()){
            RsEventPO rsEventPO = RsEventPO.builder().keyWord(rsEvent.getKeyWord())
                    .eventName(rsEvent.getEventName()).userPO(byId.get())
                    .build();
            RsEventPO save = rsRepository.save(rsEventPO);
            return ResponseEntity.ok().build();
        }

            return ResponseEntity.badRequest().build();
    }

    @PutMapping("/rs/{rsId}")
    public ResponseEntity updateRsEvent(@PathVariable Integer rsId,@RequestBody RsEvent rsEvent){

        RsEventPO rsEventPO = rsRepository.findById(rsId).get();
       if (rsEventPO.getUserPO().getId() == rsEvent.getUserId()){
           if (rsEvent.getEventName() != ""){
               rsEventPO.setEventName(rsEvent.getEventName());
           }else if (rsEvent.getKeyWord() != ""){
               rsEventPO.setKeyWord(rsEvent.getKeyWord());
           }
           rsRepository.save(rsEventPO);
           return  ResponseEntity.ok().build();
       }else {
           return ResponseEntity.badRequest().build();
       }
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity voteForRsEvent(@RequestBody @Valid Vote vote,@PathVariable Integer rsEventId){
        int voteCount = vote.getVoteCount();

        UserPO userPO = userRepository.findById(vote.getUserId()).get();
        Integer voteNum = userPO .getVoteNum();
        if (voteNum >= voteCount){
            VotePO build = VotePO.builder().createTime(vote.getCreateTime()).voteCount(voteCount)
                    .user(userPO).build();
            voteRepository.save(build);
            RsEventPO rsEventPO = rsRepository.findById(rsEventId).get();
            rsEventPO.setVoteCountNumber(voteCount + rsEventPO.getVoteCountNumber());
            rsRepository.save(rsEventPO);
            userPO.setVoteNum(userPO.getVoteNum() - voteCount);
            userRepository.save(userPO);
            return ResponseEntity.ok().build();
        }else{
            return  ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/rs/event")
    public ResponseEntity getAllEventList(){
        List<RsEventPO> all = rsRepository.findAll();
        List<RsEventVO> voLists = new ArrayList<>();
        for (RsEventPO rsEventPO :all){
            RsEventVO rsEventVO = new RsEventVO();
            BeanUtils.copyProperties(rsEventPO,rsEventVO);
            voLists.add(rsEventVO);
        }
        return ResponseEntity.ok().body(voLists);
    }

    @GetMapping("/rs/event/{id}")
    public ResponseEntity getRsEventById(@PathVariable Integer id){

        Optional<RsEventPO> rsEventPO = rsRepository.findById(id);

        if (rsEventPO.isPresent()){
            RsEventVO rsEventVO = new RsEventVO();
            BeanUtils.copyProperties(rsEventPO.get(),rsEventVO);
            return  ResponseEntity.ok().body(rsEventVO);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/rs/event/{id}")
    public ResponseEntity deleteRsEventById(@PathVariable Integer id){
        Optional<RsEventPO> rsEventPO = rsRepository.findById(id);
        if (rsEventPO.isPresent()){
            rsRepository.deleteById(id);
            return  ResponseEntity.ok().body("成功删除");
        }else{
            return  ResponseEntity.notFound().build();
        }
    }
}
