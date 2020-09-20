package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: xqc
 * @Date: 2020/9/17 - 09 - 17 - 22:32
 * @Description: com.thoughtworks.rslist.po
 * @version: 1.0
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
public class VotePO {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "vote_count")
    private Integer voteCount;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserPO user;

    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private RsEventPO rsEvent;


}
