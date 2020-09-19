package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @Author: xqc
 * @Date: 2020/9/17 - 09 - 17 - 22:52
 * @Description: com.thoughtworks.rslist.repository
 * @version: 1.0
 */
public interface VoteRepository extends PagingAndSortingRepository<VotePO,Integer> {

//    @Query(value = "select  v from  VotePO  as  v where v.user.id = :userId and v.rsEvent.id = :rsEventId")
    @Query(value = "select * from  votepo  as v where  v.user_id = :userId and v.rs_event_id = :rsEventId"
            ,nativeQuery = true)
    List<VotePO> findAllByUserIdAndRsEventId(Integer userId, Integer rsEventId, Pageable pageable);


    @Query(value = "SELECT * FROM votepo as v WHERE  v.create_time BETWEEN ?1 AND ?2",
    nativeQuery = true)
    List<VotePO> findAllByCreateTimeBetweenStartAndEnd(String  startTime, String  endTime,Pageable pageable);


    List<VotePO> findAllByUserId(Integer userId);
}
