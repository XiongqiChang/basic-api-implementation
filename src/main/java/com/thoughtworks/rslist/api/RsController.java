package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.vo.RsEventVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RsController {

    @Autowired
    private RsEventService rsEventService;



    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent){

        if (!rsEventService.addRsEvent(rsEvent)){
             return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();

    }

    @PutMapping("/rs/{rsId}")
    public ResponseEntity updateRsEvent(@PathVariable Integer rsId,@RequestBody RsEvent rsEvent){

        if (rsId < 0){
            throw new RsEventNotValidException("invalid param");
        }
        if (!rsEventService.updateRsEventById(rsId,rsEvent)){
            return  ResponseEntity.badRequest().build();
        }else {
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity voteForRsEvent(@RequestBody @Valid Vote vote,@PathVariable Integer rsEventId){

       if (rsEventId < 0){
            throw new RsEventNotValidException("invalid param");
       }
       if (!rsEventService.voteForRsEvent(vote,rsEventId)){
           return  ResponseEntity.badRequest().build();
       }
          return ResponseEntity.ok().build();
    }

    @GetMapping("/rs/event")
    public ResponseEntity getAllEventList(){
        List<RsEventVO> rsEventAll = rsEventService.getRsEventAll();
        return ResponseEntity.ok().body(rsEventAll);
    }

    @GetMapping("/rs/event/{id}")
    public ResponseEntity getRsEventById(@PathVariable Integer id){

       if (id <= 0){
           throw  new RsEventNotValidException("invalid param");
       }
        RsEventVO rsEventById = rsEventService.getRsEventById(id);
       if (rsEventById == null){
           return ResponseEntity.badRequest().build();
       }else{
           return ResponseEntity.ok().body(rsEventById);
       }
    }

    @DeleteMapping("/rs/event/{id}")
    public ResponseEntity deleteRsEventById(@PathVariable Integer id){
        if (id <= 0){
            throw new RsEventNotValidException("invalid param");
        }
        if (!rsEventService.deleteRsEventById(id)){
            return  ResponseEntity.badRequest().build();
        }else {
            return ResponseEntity.ok().build();
        }
    }

}
