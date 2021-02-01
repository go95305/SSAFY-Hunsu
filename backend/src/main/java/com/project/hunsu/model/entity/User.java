package com.project.hunsu.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column(name = "uid")
    private Long uid;

    @Column(name = "nickname",unique = true)
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
}