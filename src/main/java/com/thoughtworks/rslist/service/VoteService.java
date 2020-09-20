package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: xqc
 * @Date: 2020/9/18 - 09 - 18 - 15:33
 * @Description: com.thoughtworks.rslist.service
 * @version: 1.0
 */

@Configuration
public class VoteService {


    private VoteRepository voteRepository;
    public VoteService(VoteRepository voteRepository){
        this.voteRepository = voteRepository;
    }



    public List<Vote> getVoteRecordByUserIdAndByRsId(Integer userId, Integer rsEventId, Integer pageIndex){

        Pageable pageable = PageRequest.of(pageIndex - 1,5);

        List<VotePO> allByUserIdAndRsEventId = voteRepository.findAllByUserIdAndRsEventId(userId, rsEventId,pageable);

        List<Vote> getVoteListByUserIdAndByRsId = transferVotePoToVoteList(allByUserIdAndRsEventId);

        return getVoteListByUserIdAndByRsId;

    }



    public Vote findBVoteId(Integer id){
        Optional<VotePO> byId = voteRepository.findById(id);
        if (byId.isPresent()){
            VotePO votePO = byId.get();
            Vote vote = Vote.builder().userId(votePO.getUser().getId()).rsEventId(votePO.getRsEvent().getId())
                    .voteCount(votePO.getVoteCount()).createTime(votePO.getCreateTime()).build();
            return  vote;
        }else {
            return  null;
        }
    }


    public List<Vote> findAllByCreateTimeBetween(String  startTime, String  endTime,Integer pageIndex,Integer pageSize){

        Pageable pageable =  PageRequest.of(pageIndex -1, pageSize);
        List<VotePO> allByCreateTimeBetweenStartAndEnd = voteRepository.findAllByCreateTimeBetweenStartAndEnd(startTime, endTime,pageable);
        List<Vote> getVoteListByUserIdAndByRsId = transferVotePoToVoteList(allByCreateTimeBetweenStartAndEnd);
        return getVoteListByUserIdAndByRsId;

    }


    private  List<Vote>  transferVotePoToVoteList(List<VotePO> list){

        List<Vote> transToVotes = list.stream().map(item ->
                Vote.builder().userId(item.getUser().getId()).rsEventId(item.getRsEvent().getId())
                        .voteCount(item.getVoteCount()).createTime(item.getCreateTime()).build()
        ).collect(Collectors.toList());

        return transToVotes;

    }

    public void deleteVoteById(Integer id) {
        voteRepository.deleteById(id);
    }



    public List<Vote> getVoteListByUserId(Integer userId){
        List<VotePO> allByUserId = voteRepository.findAllByUserId(userId);
        List<Vote> votes = transferVotePoToVoteList(allByUserId);
        return votes;
    }
}
