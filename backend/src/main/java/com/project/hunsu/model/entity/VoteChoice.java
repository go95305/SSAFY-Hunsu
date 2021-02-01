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
public class VoteChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_idx")
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "voteItem_idx", nullable = false)
    private VoteItem voteItem;

    @ManyToOne
    @JoinColumn(name = "nickname")
    private User user;
}
