package com.project.hunsu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ootd_idx")
    private Ootd ootd;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wear_idx")
    private Wear wear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nickname")
    private User user;

    @Column(columnDefinition = "bigint default 0 ")
    private Long depth;

    @Column(name = "write_date")
    private LocalDateTime writeDate;

    private String content;

    @Column(name = "group_num")
    private int groupNum;
}
