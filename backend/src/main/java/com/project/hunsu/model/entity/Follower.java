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
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follower_idx")
    private Long idx;

    @ManyToOne
    @JoinColumn(name= "memId")
    private User memId;

    @ManyToOne
    @JoinColumn(name= "targetId")
    private User targetId;

    @Column(name = "flag")
    private Boolean flag;
    @PrePersist
    void preInsert(){
        if(this.flag==null)
            this.flag=true;
    }

}