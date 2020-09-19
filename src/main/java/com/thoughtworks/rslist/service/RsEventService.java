package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.vo.RsEventVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: xqc
 * @Date: 2020/9/18 - 09 - 18 - 19:31
 * @Description: com.thoughtworks.rslist.service
 * @version: 1.0
 */
@Service
public class RsEventService {
    @Autowired
    private RsRepository rsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoteRepository voteRepository;

    public boolean addRsEvent(RsEvent rsEvent) {
        String eventName = rsEvent.getEventName();
        if (rsRepository.findByEventName(eventName) != null) {
            return false;
        }
        Optional<UserPO> userPO = userRepository.findById(rsEvent.getUserId());
        if (!userPO.isPresent()) {
            return false;
        }
        RsEventPO rsEventPO = RsEventPO.builder().keyWord(rsEvent.getKeyWord())
                .eventName(rsEvent.getEventName()).user(userPO.get()).voteCountNumber(rsEvent.getVoteCountNumber())
                .build();
        RsEventPO save = rsRepository.save(rsEventPO);
        return save != null;


    }

    public boolean updateRsEventById(Integer rsEventId, RsEvent rsEvent) {
        Optional<RsEventPO> rsEventPo = rsRepository.findById(rsEventId);
        if (rsEventPo.isPresent() && rsEvent.getUserId() == rsEventPo.get().getUser().getId()) {
            RsEventPO rsEventReadyUpdate = rsEventPo.get();
            if (rsEvent.getEventName() != "") {
                rsEventReadyUpdate.setEventName(rsEvent.getEventName());
            }
            if (rsEvent.getKeyWord() != "") {
                rsEventReadyUpdate.setKeyWord(rsEvent.getKeyWord());
            }
            rsRepository.save(rsEventReadyUpdate);
            return true;
        } else {
            return false;
        }

    }


    public List<RsEventVO> getRsEventAll() {
        List<RsEventPO> all = rsRepository.findAll();
        List<RsEventVO> collect = all.stream().map(item -> RsEventVO.builder().id(item.getId()).eventName(item.getEventName())
                .keyWord(item.getKeyWord()).voteCountNumber(item.getVoteCountNumber()).build()).collect(Collectors.toList());
        return collect;
    }

    public RsEventVO getRsEventById(Integer id) {
        Optional<RsEventPO> getRsEventPo = rsRepository.findById(id);
        if (!getRsEventPo.isPresent()) {
            return null;
        } else {
            RsEventPO rsEventPO = getRsEventPo.get();
          RsEventVO buildRsEvent = RsEventVO.builder().eventName(rsEventPO.getEventName()).id(rsEventPO.getId())
                 .keyWord(rsEventPO.getKeyWord()).voteCountNumber(rsEventPO.getVoteCountNumber()).build();
            return buildRsEvent;
        }
    }

    public boolean deleteRsEventById(Integer id) {
        Optional<RsEventPO> byId = rsRepository.findById(id);
        if (!byId.isPresent()) {
            return false;
        } else {
            rsRepository.deleteById(id);
            return true;
        }

    }

    public boolean voteForRsEvent(Vote vote, Integer rsEventId) {

        Optional<RsEventPO> rsEventPoById = rsRepository.findById(rsEventId);
        if (!rsEventPoById.isPresent()) {
            return false;
        } else {
            Integer voteCount = vote.getVoteCount();
            Optional<UserPO> userPoById = userRepository.findById(vote.getUserId());
            if (!userPoById.isPresent()) {
                return false;
            } else {
                UserPO userPo = userPoById.get();
                if (userPo.getVoteNum() > voteCount) {
                    userPo.setVoteNum(userPo.getVoteNum() - voteCount);
                    userRepository.save(userPo);
                    RsEventPO rsEventPo = rsEventPoById.get();
                    rsEventPo.setVoteCountNumber(rsEventPo.getVoteCountNumber() + voteCount);
                    rsRepository.save(rsEventPo);
                    voteRepository.save(VotePO.builder().user(userPo).createTime(vote.getCreateTime()).rsEvent(rsEventPo).voteCount(voteCount).build());
                    return true;
                } else {
                    return false;
                }
            }

        }
    }
}
