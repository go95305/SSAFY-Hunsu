package com.project.hunsu.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User {

    @Id
    @Column(name = "nickname")
    private String nickname;

    @Column(unique = true)
    private String oauthId;
    private String gender;

    @Column(name = "access_token")
    private String accessToken;

    @Column
    private String providerName;
    private String age;
    private double height;
    private String size;

    public User(String oauthId, String nickname, String providerName, String accessToken) {

        this.oauthId = oauthId;
        this.nickname = nickname;
        this.providerName = providerName;
        this.accessToken = accessToken;
    }
}