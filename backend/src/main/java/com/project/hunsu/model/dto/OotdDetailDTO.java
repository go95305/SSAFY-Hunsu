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
public class OotdDetailDTO {
    private Long ootdIdx;
    private String content;
    private int count;
    private Boolean isUpdated;
    private LocalDateTime writeDate;
    private String nickname;
    private List<String> hashTag= new ArrayList<>();
    public void addHashtag(String hashtag){
        this.hashTag.add(hashtag);
    }
}