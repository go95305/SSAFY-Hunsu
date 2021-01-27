package com.project.hunsu.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OotdMain {
    private Long ootdIdx; //ootd에서 가져오기
    private String nickname; //ootd에서 가져오기
    private String ootdContent; //ootd에서 가져오기
    private String hashtagContent; //hashtag에서 가져오기
    private int ootdLike;//ootd에서 가져오기

//    @QueryProjection
//    public OotdMain(Long ootdIdx, String nickname, String ootdContent, String hashtagContent, int ootdLike) {
//        this.ootdIdx = ootdIdx;
//        this.nickname = nickname;
//        this.ootdContent = ootdContent;
//        this.hashtagContent = hashtagContent;
//        this.ootdLike = ootdLike;
//    }

    public OotdMain() {
    }
}
