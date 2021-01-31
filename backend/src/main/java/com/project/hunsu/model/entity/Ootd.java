package com.project.hunsu.model.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "nickname")
    private User user;

//    @OneToMany(mappedBy = "ootd")
//    private Hashtag hashtag;

//    public void addHashtag(Hashtag hashtag){
//        hashtagList.add(hashtag);
//        hashtag.setOotd(this);
//    }

    private String content;

    @Column(name = "is_updated")
    private Boolean isUpdated;

    @CreationTimestamp
    @Column(name = "write_date")
    private LocalDateTime writeDate;

    //likeCount로 바꾸기
    private int count;

    @Column(name = "isActived",columnDefinition = "boolean default false")
    private Boolean isActivated;


}
