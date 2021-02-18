package com.hindsight.authdemo.model.social;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoToken {
    private String accessToken;
    private String refreshToken;
}
