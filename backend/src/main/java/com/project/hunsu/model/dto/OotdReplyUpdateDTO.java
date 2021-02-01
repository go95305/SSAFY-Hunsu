package com.project.hunsu.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OotdReplyUpdateDTO {
    private String content;
    private Long replyIdx;
}
