package com.project.hunsu.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OotdDetailDTO {
    private Long ootdIdx;
    private String content;
    private int count;
    private Boolean isUpdated;
    private LocalDateTime writeDate;
    private String nickname;
}
