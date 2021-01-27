package com.project.hunsu.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "ootd_idx")
    private Ootd ootd;

    @Column(columnDefinition = "boolean default 1")
    private boolean ootdActive;
    @Column(columnDefinition = "boolean default 1")
    private boolean wearActive;

    @ManyToOne
    @JoinColumn(name = "wear_idx")
    private Wear wear;

    @Column(columnDefinition = "bigint default 0 ")
    private Long depth;

    @Column(name = "write_date")
    private LocalDateTime writeDate;

    private String content;

    @Column(name = "group_num")
    private int groupNum;
}
