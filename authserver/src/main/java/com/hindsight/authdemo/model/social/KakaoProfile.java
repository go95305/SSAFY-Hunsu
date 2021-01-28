package com.hindsight.authdemo.model.social;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Properties;

@Getter
@Setter
@ToString
public class KakaoProfile {
    private Long id;
    private Properties properties;

    @Getter
    @Setter
    @ToString
    private static class Properties{ // 추후 성별, 연령대 추가 가능
        private String nickname;
        private String thumbnail_image;
        private String profile_image;
    }

}
