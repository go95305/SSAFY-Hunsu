package com.project.hunsu.model.dto;

import com.project.hunsu.model.entity.OotdReply;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class OotdDetailDTO {
    private Long ootdIdx;
    private String content;
    private int likeCount;
    private Boolean isUpdated;
    private LocalDateTime writeDate;
    private String nickname;
    private List<String> hashTagList = new ArrayList<>();
    private Boolean likeChk;
    private List<OotdReplyDTO> replyDTOList = new ArrayList<>();
    public void addHashtag(String hashtag) {
        this.hashTagList.add(hashtag);
    }


    private List<OotdReplyDTO> ootdReplyDTOList = new ArrayList<>();

    public void addReply(OotdReplyDTO ootdReplyDTO) {
        this.ootdReplyDTOList.add(ootdReplyDTO);
    }
}