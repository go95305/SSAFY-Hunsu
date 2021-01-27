package com.project.hunsu.Controller;

import com.project.hunsu.Dto.OotdDetail;
import com.project.hunsu.Dto.OotdLikeCount;
import com.project.hunsu.Dto.OotdMain;
import com.project.hunsu.Entity.Hashtag;
import com.project.hunsu.Entity.Ootd;
import com.project.hunsu.Dto.OotdUpdate;
import com.project.hunsu.Entity.Reply;
import com.project.hunsu.Service.OotdService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;


@RestController
public class OotdController {
    private final OotdService ootdService;

    public OotdController(OotdService ootdService) {
        this.ootdService = ootdService;
    }

    @PersistenceContext    // EntityManagerFactory가 DI 할 수 있도록 어노테이션 설정
    private EntityManager entityManager;

    @GetMapping("/ootd/{sort}")  //ootd_idx, 닉네임, 글내용, 해시태그, 좋아요 개수
    @ApiOperation(value = "Ootd 메인페이지")
    public List<OotdMain> ootdSortedList(@PathVariable int sort) {
        System.out.println(sort);
        List<OotdMain> ootdList=ootdService.SortByRecentOrPopularity(sort);

        for (int i = 0; i < ootdList.size(); i++) {
            System.out.println(ootdList.get(i));
        }
        return ootdList;
    }

    @GetMapping("/ootd/detail/{ootdIdx}") // 에러.. 찾아보자 // ootd_idx,content,count,is_updated,write_date,nickname
    @ApiOperation(value = "Ootd 상세페이지")
    public OotdDetail detailOotd(@PathVariable("ootdIdx") Long ootdIdx) {
//        Ootd ootdDetail = ootdRepository.findByIdx(ootdidx);
        OotdDetail ootdDetail = ootdService.SpecificOotd(ootdIdx);
        System.out.println(ootdDetail);
        return ootdDetail;
    }


    @PutMapping("/ootd")
    @Transactional
    @ApiOperation(value = "Ootd 글수정")
    public void updateOotd(@Valid @RequestBody OotdUpdate ootdUpdate) {
        Hashtag hashtag = entityManager.find(Hashtag.class, ootdUpdate.getOotdIdx());
        hashtag.setHashtag(ootdUpdate.getHashtag());
        Ootd ootd = hashtag.getOotd();
        ootd.setContent(ootdUpdate.getContent());
        ootd.setUpdated(true);
    }


    @DeleteMapping("/ootd")
    @Transactional
    @ApiOperation(value = "Ootd 글삭제")
    public void deleteOotd(@RequestParam(required = true) Long idx) {
        Reply reply = entityManager.find(Reply.class, idx);
        reply.setOotdActive(false);
        reply.setWearActive(false);
//        ootdRepository.deleteByIdx(idx);
    }

    @PutMapping("/ootd/like")
    @Transactional
    @ApiOperation(value = "Ootd글 좋아요")
    public void ootdLike(@Valid @RequestBody OotdLikeCount ootdLikeCount) {
        Ootd ootd = entityManager.find(Ootd.class, ootdLikeCount.getOotd_idx());
        if (ootdLikeCount.getChk()) {//좋아요 +1
            ootd.setCount(ootd.getCount() + 1);
        } else {// 좋아요 -1
            ootd.setCount(ootd.getCount() - 1);
        }
//        ootdRepository.save(ootd);
    }

}
