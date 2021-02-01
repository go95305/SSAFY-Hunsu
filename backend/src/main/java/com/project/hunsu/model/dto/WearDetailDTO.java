package com.project.hunsu.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class WearDetailDTO {
    private Long wear_idx;
    private String title;
    private String content;
    private Boolean vote_activated;
    private LocalDateTime write_date;
    private String nickname;
    private List<Reply> replyList=new ArrayList<>();
}
