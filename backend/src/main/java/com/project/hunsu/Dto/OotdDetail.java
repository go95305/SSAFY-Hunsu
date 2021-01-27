package com.project.hunsu.Dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OotdDetail {
    private Long ootd_idx;
    private String content;
    private int count;
    private Boolean is_updated;
    private LocalDateTime write_date;
    private String nickname;
}
