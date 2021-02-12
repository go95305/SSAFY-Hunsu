package com.project.hunsu.model.dto;

import com.project.hunsu.model.entity.Ootd;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class MyPageDetailDTO {
    private String mypageNickname;
    private Boolean mypage;
    private Long uid;
    private Boolean follow;
    private List<Long> ootd_like_list=new ArrayList<>();
    private List<Long> ootd_list=new ArrayList<>();
    private List<String> follower_list=new ArrayList<>();
    private List<String> following_list=new ArrayList<>();
}