package com.project.hunsu.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WearReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_idx")
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "wear_idx")
    private Wear wear;

    @ManyToOne
    @JoinColumn(name = "nickname")
    private User user;

    @Column(columnDefinition = "bigint default 0 ")
    private Long depth;

    @Column(columnDefinition = "timestamp not null default now()", name = "write_date",insertable = false, updatable = false)
    private LocalDateTime writeDate;

    private String content;

    @Column(name = "group_num")
    private Long groupNum;

    private int count;

    @Column(name = "flag")
    private Boolean flag;

    @PrePersist
    void preInsert(){
        if(this.flag==null)
            this.flag=true;
    }
}
