package com.project.hunsu.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class WearValue {
    private Integer num;
    //투표 사진 갯수
    private LocalDateTime endtime;
    private String nickname;
    private String title;
    private String content;
}
