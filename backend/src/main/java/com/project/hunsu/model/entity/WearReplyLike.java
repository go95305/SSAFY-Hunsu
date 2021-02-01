package com.project.hunsu.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WearReplyLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "replyLike_idx")
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "reply_idx")
    private Reply reply;

    @ManyToOne
    @JoinColumn(name = "nickname")
    private User user;

    @Column(name = "flag")
    private Boolean flag;

    @PrePersist
    void preInsert(){
        if(this.flag==null)
            this.flag=true;
    }
}
