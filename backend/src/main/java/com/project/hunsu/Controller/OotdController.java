package com.project.hunsu.Controller;

import com.project.hunsu.Entity.Hashtag;
import com.project.hunsu.Entity.Ootd;
import com.project.hunsu.Dto.OotdFix;
import com.project.hunsu.Entity.Reply;
import com.project.hunsu.kakao.Repository.OotdRepository;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class OotdController {

    private final OotdRepository ootdRepository;

    public OotdController(OotdRepository ootdRepository) {
        this.ootdRepository = ootdRepository;
    }

    @PersistenceContext    // EntityManagerFactory가 DI 할 수 있도록 어노테이션 설정
    private EntityManager entityManager;

    @GetMapping("/ootd/{sort}")  //ootd_idx, 닉네임, 글내용, 해시태그, 좋아요 개수
    public List<Ootd> ootdMain(@PathVariable int sort) {
        List<Ootd> ootdList = new ArrayList<>();
        if (sort == 0) {
            //최신순
            ootdList = ootdRepository.findOotdByOrderByWriteDate();
        } else {
            //인기순
            ootdList = ootdRepository.findOotdByOrderByCountDesc();
        }
        for (int i = 0; i < ootdList.size(); i++) {
            System.out.println(ootdList.get(i).getIdx());
        }
        return ootdList;
    }

    @GetMapping("/ootd/detail/{idx}") // 에러.. 찾아보자
    public Ootd detailOotd(@PathVariable Long idx) {
        Ootd ootdDetail = ootdRepository.findByIdx(idx);
        System.out.println(ootdDetail);
        return ootdDetail;
    }


    @PutMapping("/ootd")
    @Transactional
    public void updateOotd(@Valid @RequestBody OotdFix ootdFix){
        Hashtag hashtag = entityManager.find(Hashtag.class, ootdFix.getOotdIdx());
        hashtag.setHashtag(ootdFix.getHashtag());
        Ootd ootd = hashtag.getOotd();
        ootd.setContent(ootdFix.getContent());
        ootd.setUpdated(true);
    }


    @DeleteMapping("/ootd")
    @Transactional
    public void deleteOotd(@RequestParam(required = true) Long idx){
        Reply reply = entityManager.find(Reply.class, idx);
        reply.setOotdActive(false);
        reply.setWearActive(false);
        ootdRepository.deleteByIdx(idx);
    }

    @PutMapping("/ootd/like")
    public void ootdLike(@RequestParam(required = true) Long idx){
        Ootd ootd = ootdRepository.findByIdx(idx);
        ootd.setCount(ootd.getCount()+1);
        ootdRepository.save(ootd);
    }

}
