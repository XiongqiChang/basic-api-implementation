package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
           if (rsEvent.getKeyWord() != ""){
               rsEventPO.setKeyWord(rsEvent.getKeyWord());
           }else if (rsEvent.getKeyWord() != ""){
               rsEventPO.setEventName(rsEvent.getEventName());
           }
           rsRepository.save(rsEventPO);
           return  ResponseEntity.ok().build();
       }else {
           return ResponseEntity.badRequest().build();
       }

    }

}
