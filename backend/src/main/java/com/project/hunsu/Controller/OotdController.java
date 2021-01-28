package com.project.hunsu.Controller;

import com.project.hunsu.Dto.*;
import com.project.hunsu.Entity.*;
import com.project.hunsu.Service.OotdService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class OotdController {
    private final OotdService ootdService;

    public OotdController(OotdService ootdService) {
        this.ootdService = ootdService;
    }

    @PersistenceContext    // EntityManagerFactory가 DI 할 수 있도록 어노테이션 설정
    private EntityManager entityManager;

    @GetMapping("/ootd/{sort}")  //ootd_idx, 닉네임, 글내용, 해시태그, 좋아요 개수
    @ApiOperation(value = "Ootd 메인페이지")//안됨
    public List<OotdMain> ootdSortedList(@PathVariable int sort) {
        System.out.println(sort);
        List<OotdMain> ootdMainList = ootdService.SortByRecentOrPopularity(sort);
        return ootdMainList;
    }

    @GetMapping("/ootd/detail/{ootdIdx}") // 에러.. 찾아보자 // ootd_idx,content,count,is_updated,write_date,nickname
    @ApiOperation(value = "Ootd 상세페이지") // 이것도 jpql 아니면 querydSL써야함
    public OotdDetail detailOotd(@PathVariable("ootdIdx") Long ootdIdx) {
//        Ootd ootdDetail = ootdRepository.findByIdx(ootdidx);
        OotdDetail ootdDetail = ootdService.SpecificOotd(ootdIdx);
        System.out.println(ootdDetail);
        return ootdDetail;
    }


    @PutMapping("/ootd")
    @Transactional
    @ApiOperation(value = "Ootd 글수정")//이건 된다.
    public void updateOotd(@Valid @RequestBody OotdUpdate ootdUpdate) {
        Hashtag hashtag = entityManager.find(Hashtag.class, ootdUpdate.getOotdIdx());
        if (hashtag != null) {
            hashtag.setContent(ootdUpdate.getHashtag());
        }
        Ootd ootd = hashtag.getOotd();
        ootd.setContent(ootdUpdate.getContent());
        ootd.setUpdated(true);
    }


    @DeleteMapping("/ootd/{idx}")
    @Transactional
    @ApiOperation(value = "Ootd 글삭제")//안됨
    public void deleteOotd(@PathVariable Long idx) {
        ootdService.delete(idx);
    }

    @PutMapping("/ootd/like")
    @Transactional
    @ApiOperation(value = "Ootd글 좋아요") // 성공
    public int ootdLike(@Valid @RequestBody OotdLikeCount ootdLikeCount) {
        Ootd ootd = entityManager.find(Ootd.class, ootdLikeCount.getOotdIdx());
        User user = entityManager.find(User.class, ootdLikeCount.getNickname());
        if (ootdLikeCount.getChk()) {//좋아요 +1
            ootd.setCount(ootd.getCount() + 1);
            OotdLike ootdLike = new OotdLike();
            ootdLike.setOotd(ootd);
            ootdLike.setUser(user);
            entityManager.persist(ootdLike);
        } else {// 좋아요 -1
            ootd.setCount(ootd.getCount() - 1);
            ootdService.likedown(ootd.getIdx(), user.getNickname());
        }
        return ootd.getCount();
    }

    @GetMapping("/ootd/hashtag/{hashtag}") // 해결
    @ApiOperation(value = "Ootd 해시태그기반 검색")
    public List<Ootd> hashtagSearch(@PathVariable String hashtag) {
        List<Ootd> ootdList = ootdService.searchByHashtag(hashtag);
        return ootdList;
    }

    @PostMapping("/ootd")
    @ApiOperation(value = "Ootd글 작성")
    public ResponseEntity<Map<String,Object>> writeOotd(@Valid @RequestBody OotdWrite ootdWrite){
        ResponseEntity<Map<String, Object>> resEntity = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ootdService.write(ootdWrite);
            map.put("msg","success");
            resEntity= new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
        }catch (Exception e){
            map.put("msg","fail");
            resEntity= new ResponseEntity<Map<String,Object>>(map, HttpStatus.FORBIDDEN);
        }
        return resEntity;
    }
}
