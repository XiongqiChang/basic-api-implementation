package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        VotePO build = VotePO.builder().createTime(vote.getCreateTime()).voteCount(vote.getVoteCount())
                .userPO(userRepository.findById(vote.getUserId()).get()).build();
        voteRepository.save(build);
        RsEventPO rsEventPO = rsRepository.findById(rsEventId).get();
        rsEventPO.setVoteCountNumber(vote.getVoteCount());
        rsRepository.save(rsEventPO);
        return ResponseEntity.ok().build();

    }

}
