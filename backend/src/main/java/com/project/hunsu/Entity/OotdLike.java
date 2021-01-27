package com.project.hunsu.Entity;

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
public class OotdLike {

    @Id
    @Column(name = "like_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "ootd_idx")
    private Long ootdIdx;

    @Column(name = "nickname")
    private String nickname;


}
