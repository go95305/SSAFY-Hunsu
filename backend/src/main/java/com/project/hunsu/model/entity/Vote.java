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
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_idx")
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "wear_idx")
    private Wear wear;

    @Column(columnDefinition = "timestamp not null default now()", name = "start_time",insertable = false, updatable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time",insertable = true, updatable = false)
    private LocalDateTime endTime;

}
