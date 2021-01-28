package com.hindsight.authdemo.model.social;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SocialSignUp {
    private String provider;
    private String accessToken;
    private String nickname;
}
