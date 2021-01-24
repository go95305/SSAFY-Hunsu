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
    private LocalDateTime write_date;

    @Column(name = "is_updated")
    private boolean isUpdated;
}
