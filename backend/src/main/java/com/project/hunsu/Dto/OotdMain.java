package com.project.hunsu.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OotdMain {
    private Long ootdIdx;
    private String nickname;
    private String ootdContent;
    private String hashtagContent;
    private int ootdLike;

    @QueryProjection
    public OotdMain(Long ootdIdx, String nickname, String ootdContent, String hashtagContent, int ootdLike) {
        this.ootdIdx = ootdIdx;
        this.nickname = nickname;
        this.ootdContent = ootdContent;
        this.hashtagContent = hashtagContent;
        this.ootdLike = ootdLike;
    }
}
