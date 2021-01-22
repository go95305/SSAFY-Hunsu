package com.project.hunsu.dto;

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
public class ootd {

    @Id
    @Column(name = "ootd_idx")
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "nickname")
    private User user;

    private String content;

    @Column(name = "is_updated")
    private boolean isupdated;

    @Column(name = "write_date")
    private LocalDateTime writeDate;

    private int count;
}
