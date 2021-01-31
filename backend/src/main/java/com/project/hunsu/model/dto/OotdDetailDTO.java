package com.project.hunsu.model.dto;

import com.project.hunsu.model.entity.Reply;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class OotdDetailDTO {
    private Long ootdIdx;
    private String content;
    private int count;
    private Boolean isUpdated;
    private LocalDateTime writeDate;
    private String nickname;
    private String hashTag;
    private List<Reply> replyList;
}
