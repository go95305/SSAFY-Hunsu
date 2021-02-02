package com.project.hunsu.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable { //왜 Serializable쓰는지 정리하자! ==> Unique key를 외래키로 받으면 생기는데 왜??
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column(name = "uid")
    private String uid;
    private String nickname;

    private Boolean flag;
    private String gender;

    @Column(name = "access_token")
    private String accessToken;
    private String refreshToken;

    @Column
    private String age;
    private double height;
    private String size;
    private String jwtRefresh;
    private String jwtAccess;
}