package com.project.hunsu.model.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.function.Function;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Ootd {

    @Id
    @Column(name = "ootd_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "nickname",referencedColumnName = "nickname")
    private User user;

    private String content;

    @Column(name = "is_updated")
    private Boolean isUpdated;

    @CreationTimestamp
    @Column(name = "write_date")
    private LocalDateTime writeDate;

    //likeCount로 바꾸기
    private int count;

    @Column(name = "flag")
    private Boolean flag;

    @PrePersist
    void preInsert(){
        if(this.flag==null)
            this.flag=true;
    }


}