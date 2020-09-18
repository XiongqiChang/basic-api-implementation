package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xqc
 * @Date: 2020/9/18 - 09 - 18 - 14:29
 * @Description: com.thoughtworks.rslist.api
 * @version: 1.0
 */
@RestController
public class VoteController {

    @Autowired
    private VoteService voteService;


    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecord(@RequestParam Integer userId,
                                                    @RequestParam Integer rsEventId,
                                                    @RequestParam Integer pageIndex){

        if (userId < 0 || rsEventId < 0 || pageIndex <= 0){
            return ResponseEntity.badRequest().build();
        }
        List<Vote> voteRecordByUserIdAndByRsId = voteService.getVoteRecordByUserIdAndByRsId(userId, rsEventId,pageIndex);
        return  ResponseEntity.ok(voteRecordByUserIdAndByRsId);
    }

    @GetMapping("/getVote/{voteId}")
    public ResponseEntity<Vote> getVotePo(@PathVariable Integer voteId){
        Vote voteById = voteService.findBVoteId(voteId);
        return ResponseEntity.ok().body(voteById);
    }


    @GetMapping("/records")
    public ResponseEntity<List<Vote>> getVoteByCreateTime(@RequestParam String  startTime,
                                                          @RequestParam String  endTime,
                                                          @RequestParam Integer pageIndex,
                                                          @RequestParam Integer pageSize){

        if (pageIndex <= 0 || pageSize <= 0){
            return ResponseEntity.badRequest().build();
        }
        List<Vote> allByCreateTimeBetween = voteService.findAllByCreateTimeBetween(startTime, endTime,pageIndex,pageSize);
        return  ResponseEntity.ok(allByCreateTimeBetween);

    }

    @DeleteMapping("/vote/{id}")
    public ResponseEntity deleteVoteById(@PathVariable Integer id){
        Vote vote = voteService.findBVoteId(id);
        if (vote == null){
            return ResponseEntity.badRequest().build();
        }
        voteService.deleteVoteById(id);
        return ResponseEntity.ok().build();
    }
}
