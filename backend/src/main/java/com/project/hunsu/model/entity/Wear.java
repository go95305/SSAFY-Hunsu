package com.project.hunsu.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Wear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wear_idx")
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "nickname",referencedColumnName = "nickname")
    private User user;

    private String title;
    private String content;

    @Column(columnDefinition = "timestamp not null default now()", name = "write_date",insertable = false, updatable = false)
    private LocalDateTime writeDate;

    @Column(name = "is_updated")
    private Boolean isUpdated;

    @Column(name = "vote_activated")
    private boolean voteActivated;

    @Column(name = "flag")
    private Boolean flag;

    @PrePersist
    void preInsert(){
        if(this.flag==null)
            this.flag=true;
    }

}
