package com.hindsight.authdemo.model.social;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtTokenform {
    private String jwtToken;
    private String jwtRefresh;
}
