package com.hindsight.authdemo.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TokenResult<T> extends CommonResult {
    @ApiModelProperty(value="jwtToken")
    private String jwtToken;

    @ApiModelProperty(value="jwtRefresh")
    private String jwtRefresh;

    @ApiModelProperty(value="nickname")
    private String nickname;
}

