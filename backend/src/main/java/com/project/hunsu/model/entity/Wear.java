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
public class Wear {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wear_idx")
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "nickname")
    private User user;

    private String title;
    private String content;


    @Column(columnDefinition = "timestamp not null default now()", name = "write_date",insertable = false, updatable = false)
    private LocalDateTime writeDate;

    @Column(name = "vote_activated")
    private boolean voteActivated;

}
