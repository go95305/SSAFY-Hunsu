package com.hindsight.authdemo.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TokenResult<T> extends CommonResult {
    @ApiModelProperty(value="jwtToken")
    private String accessToken;

    @ApiModelProperty(value="jwtRefresh")
    private String refreshToken;

    @ApiModelProperty(value="nickname")
    private String nickname;
}

