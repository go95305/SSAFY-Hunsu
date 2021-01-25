package com.project.hunsu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_idx")
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "ootd_idx")
    private Ootd ootd;

    @ManyToOne
    @JoinColumn(name = "wear_idx")
    private Wear wear;

    @ManyToOne
    @JoinColumn(name = "nickname")
    private User user;

    @ManyToOne
    @JoinColumn(name = "reply_idx",insertable = false,updatable = false)
    private Reply reply;

    @Column(columnDefinition = "bigint default 0 ")
    private Long parent_idx;




    private String content;
    private int count;
}
