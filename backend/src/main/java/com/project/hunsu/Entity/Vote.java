package com.project.hunsu.Entity;

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

    private String title;

    @Column(name = "start_time",insertable = false, updatable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time",insertable = false, updatable = false)
    private LocalDateTime endTime;

    @Column(name = "is_activated")
    private boolean isActivated;


}
