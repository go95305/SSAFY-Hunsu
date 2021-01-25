package com.project.hunsu.dto;

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
public class VoteItemChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_idx")
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "voteItem_idx")
    private VoteItem voteItem;

    @ManyToOne
    @JoinColumn(name = "nickname")
    private User user;
}
