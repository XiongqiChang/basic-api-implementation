package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.RsEventPO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @Author: xqc
 * @Date: 2020/9/17 - 09 - 17 - 16:30
 * @Description: com.thoughtworks.rslist.repository
 * @version: 1.0
 */
public interface RsRepository extends CrudRepository<RsEventPO,Integer> {

    @Override
    List<RsEventPO> findAll();



}
