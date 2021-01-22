package com.project.hunsu.dto;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "nick_name")
    private String nickname;

    @Column(unique = true)
    private Long id;
    private String gender;

    @Column(name = "access_token")
    private String accessToken;


    private String age;
    private double height;
    private String size;
}
