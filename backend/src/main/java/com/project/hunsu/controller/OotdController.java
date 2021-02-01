package com.project.hunsu.controller;

import com.project.hunsu.model.entity.Ootd;
import com.project.hunsu.model.entity.OotdLike;
import com.project.hunsu.model.entity.User;
import com.project.hunsu.service.OotdService;
import com.project.hunsu.model.dto.*;
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
@CrossOrigin(origins = {"*"})
public class OotdController {
    private final OotdService ootdService;

    public OotdController(OotdService ootdService) {
        this.ootdService = ootdService;
    }

    @PersistenceContext    // EntityManagerFactory가 DI 할 수 있도록 어노테이션 설정
    private EntityManager entityManager;

    @GetMapping("/ootd/{sort}")  //ootd_idx, 닉네임, 글내용, 해시태그, 좋아요 개수
    @ApiOperation(value = "Ootd 메인페이지 (O)", notes = "0 혹은 1값을 보내면 \n" +
            "                                           0: 최신순으로 리턴 \n" +
            "                                           1: 인기순으로 리턴 \n" +
            "                                           Parameter: 0 or 1\n" +
            "                                           Response:  ootdIdx,닉네임,글내용,해시태그,좋아요 개수")
//안됨
    public List<OotdMainDTO> ootdSortedList(@PathVariable int sort) {
        System.out.println(sort);
        List<OotdMainDTO> ootdMainDTOList = ootdService.SortByRecentOrPopularity(sort);
        return ootdMainDTOList;
    }

    @GetMapping("/ootd/detail/{ootdIdx}") // ootd_idx,content,count,is_updated,write_date,nickname
    @ApiOperation(value = "Ootd 상세페이지 (~)", notes = "ootd글에 대한 상세페이지, ootd메인페이지에서 특정 글을 클릭시\n" +
            "             해당 글에 대한 상세정보를 보여준다. 글의 ootdidx를 통해 연관된 hashtag,좋아요,댓글, 대댓글을 전부 리턴해준다.\n" +
            "             Parameter: OotdIdx \n" +
            "             Response:  ootdIdx, content, count, isUpdated, writeDate, nickname, hashtag(list), reply(list)")
    // 이것도 jpql 아니면 querydSL써야함
    public OotdDetailDTO detailOotd(@PathVariable("ootdIdx") Long ootdIdx) {
//        Ootd ootdDetail = ootdRepository.findByIdx(ootdidx);
        OotdDetailDTO ootdDetailDTO = ootdService.SpecificOotd(ootdIdx);
        return ootdDetailDTO;
    }


    @PutMapping("/ootd")
    @Transactional
    @ApiOperation(value = "Ootd 글수정 (O)", notes = "Ootd상세 페이지에서 글 수정\n" +
            "                                       Parameter: ootdidx, hashtag, content \n" +
            "                                       response:  success or fail")
    public ResponseEntity<Map<String, Object>> updateOotd(@Valid @RequestBody OotdUpdateDTO ootdUpdateDTO) {
        ResponseEntity<Map<String, Object>> resEntity = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ootdService.updateHashtag(ootdUpdateDTO);
            map.put("msg", "success");
            resEntity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        } catch (Exception e) {
            map.put("msg", "fail");
            resEntity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        }
        return resEntity;
    }


    //삭제하지 말고 비활성화 하는것으로 바꾸자.
    @DeleteMapping("/ootd/{idx}")
    @Transactional
    @ApiOperation(value = "Ootd 글삭제(비활성화) (O)", notes = "해당 글의 ootdidx를 통해 해당글을 비활성화. 연관매핑이 되어있으므로 " +
            "연관된 테이블에 정보를 우선적으로 비활성화하고  해당 글을 비활성화\n" +
            "                                               Parameter: ootdidx\n" +
            "                                               Response: success or fail")
    public ResponseEntity<Map<String, Object>> deleteOotd(@PathVariable Long idx) {
        ResponseEntity<Map<String, Object>> resEntity = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ootdService.delete(idx); //글 비활성화
            map.put("msg", "success");
            resEntity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        } catch (Exception e) {
            map.put("msg", "fail");
            resEntity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        }
        return resEntity;
    }

    @PutMapping("/ootd/like")
    @Transactional
    @ApiOperation(value = "Ootd글 좋아요 (O)", notes = "ootd글 좋아요 추가 혹은 제거 \n" +
            "                                          Parameter: nickname,ootdidx,boolean(true: 좋아요+1 false: 좋아요-1)\n" +
            "                                          Response: 수정된 좋아요 개수(count)") // 성공
    public int ootdLike(@Valid @RequestBody OotdLikeCountDTO ootdLikeCountDTO) {
        Ootd ootd = entityManager.find(Ootd.class, ootdLikeCountDTO.getOotdIdx());
        User user = entityManager.find(User.class, ootdLikeCountDTO.getNickname());
        if (ootdLikeCountDTO.getChk()) {//좋아요 +1
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


    @GetMapping("/ootd/hashtag/{hashtag}") //
    @ApiOperation(value = "Ootd 해시태그기반 검색 (O)", notes = "ootd상세글 혹은 검색에서 해시태그를 클릭 혹은 입력시 해당 해시태그가 포함된 글을 전부 리턴해준다.\n" +
            "                                                Parameter: hashtag\n" +
            "                                                Response: List<OotdMainDTO>")
    public List<OotdMainDTO> hashtagSearch(@PathVariable String hashtag) {
        List<OotdMainDTO> ootdMainDTOList = ootdService.searchByHashtag(hashtag);
        return ootdMainDTOList;
    }

    @PostMapping("/ootd")
    @ApiOperation(value = "Ootd글 작성 (O)", notes = "Parameter: nickName, content, hashtag\n" +
            "                                        Response: success or fail")
    public ResponseEntity<Map<String, Object>> writeOotd(@Valid @RequestBody OotdWriteDTO ootdWriteDTO) {
        ResponseEntity<Map<String, Object>> resEntity = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ootdService.write(ootdWriteDTO);
            map.put("msg", "success");
            resEntity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        } catch (Exception e) {
            map.put("msg", "fail");
            resEntity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        }
        return resEntity;
    }

//    @PostMapping
//    @ApiOperation(value = "Ootd글 댓글 작성")
//    public List<ReplyDTO>
}
