package com.project.hunsu.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class WearDetail {
    private Long wear_idx;
    private String title;
    private String content;
    private Boolean vote_activated;
    private LocalDateTime write_date;
    private String nickname;
}
