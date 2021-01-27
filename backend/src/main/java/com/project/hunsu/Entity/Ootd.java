package com.project.hunsu.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ootd {

    @Id
    @Column(name = "ootd_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nickname")
    private User user;

    @OneToMany(mappedBy = "ootd",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Hashtag> hashtagList = new ArrayList<>();

//    public void addHashtag(Hashtag hashtag){
//        hashtagList.add(hashtag);
//        hashtag.setOotd(this);
//    }

    private String content;

    @Column(name = "is_updated")
    private boolean isUpdated;

    @Column(name = "write_date",insertable = false, updatable = false)
    private LocalDateTime writeDate;

    private int count;
}
