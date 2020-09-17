package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.repository.CrudRepository;

/**
 * @Author: xqc
 * @Date: 2020/9/17 - 09 - 17 - 22:52
 * @Description: com.thoughtworks.rslist.repository
 * @version: 1.0
 */
public interface VoteRepository extends CrudRepository<VotePO,Integer> {

}
