package com.hindsight.authdemo.model.social;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SocialSignUp {
    private long UID;
    private String nickname;
    private boolean flag;
    private double height;
    private String size;

}
