package com.hindsight.authdemo.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TokenResult<T> extends CommonResult {
    @ApiModelProperty(value="jwtAccessToken")
    private String accessToken;

    @ApiModelProperty(value="jwtRefreshToken")
    private String refreshToken;
}

