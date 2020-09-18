package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * @Author: xqc
 * @Date: 2020/9/17 - 09 - 17 - 16:25
 * @Description: com.thoughtworks.rslist.po
 * @version: 1.0
 */
@Entity
@Data
@Table(name = "rs_event")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEventPO {

    @GeneratedValue
    @Id
    private int id;


    @Column(name = "key_word")
    private  String keyWord;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "vote_count")
    private Integer voteCountNumber;

    @ManyToOne
    private UserPO userPO;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "rsEventPO")
    private List<VotePO> votePOS;

}
