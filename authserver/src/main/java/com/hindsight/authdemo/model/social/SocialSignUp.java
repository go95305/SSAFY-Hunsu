package com.hindsight.authdemo.model.social;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SocialSignUp {
    private String UID;
    private String nickname;
    private String gender;
    private String access_token;
    private String refresh_token;
    private String age;
    private double height;
    private String size;
    private boolean flag;
//    private String provider;
//    private String accessToken;
//    private String nickname;
}
