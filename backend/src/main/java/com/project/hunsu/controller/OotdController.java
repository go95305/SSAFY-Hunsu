package com.project.hunsu.controller;

import com.project.hunsu.model.entity.Ootd;
import com.project.hunsu.model.entity.OotdLike;
import com.project.hunsu.model.entity.OotdReply;
import com.project.hunsu.service.OotdService;
import com.project.hunsu.model.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
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
    public List<OotdMainDTO> ootdSortedList(@PathVariable int sort) {
        System.out.println(sort);
        List<OotdMainDTO> ootdMainDTOList = ootdService.SortByRecentOrPopularity(sort);
        return ootdMainDTOList;
    }

    @GetMapping("/ootd/detail/{ootdIdx}") // ootd_idx,content,count,is_updated,write_date,nickname
    @ApiOperation(value = "Ootd 상세페이지 (O)", notes = "ootd글에 대한 상세페이지, ootd메인페이지에서 특정 글을 클릭시\n" +
            "             해당 글에 대한 상세정보를 보여준다. 글의 ootdidx를 통해 연관된 hashtag,좋아요,댓글, 대댓글을 전부 리턴해준다.\n" +
            "             Parameter: OotdIdx \n" +
            "             Response:  ootdIdx, content, count, isUpdated, writeDate, nickname, hashtag(list), ootdReply(list)")
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
    public String updateOotd(@Valid @RequestBody OotdUpdateDTO ootdUpdateDTO) {
        Ootd ootd = entityManager.find(Ootd.class, ootdUpdateDTO.getOotdIdx());
        String state = "";
        if (ootd != null) {
            ootdService.updateHashtag(ootdUpdateDTO);
            state = "success";
        } else
            state = "fail";
        return state;
    }


    //삭제하지 말고 비활성화 하는것으로 바꾸자.
    @DeleteMapping("/ootd/{idx}")
    @Transactional
    @ApiOperation(value = "Ootd 글삭제(비활성화) (O)", notes = "해당 글의 ootdidx를 통해 해당글을 비활성화. 연관매핑이 되어있으므로 " +
            "연관된 테이블에 정보를 우선적으로 비활성화하고  해당 글을 비활성화\n" +
            "                                               Parameter: ootdidx\n" +
            "                                               Response: success or fail")
    public String deleteOotd(@PathVariable Long idx) {
        boolean flag = ootdService.deleteOotd(idx); //글 비활성화
        if (flag)
            return "success";
        else
            return "fail";
    }

    /**
     * 누가 좋아요 했는지 정보를 사용자마다 보이는 화면이 다르니깐 해당 nickname의 유저가 좋아요한 글의 리스트를 보내준다.
     */
    @PutMapping("/ootd/like")
    @Transactional
    @ApiOperation(value = "Ootd글 좋아요 (O)", notes = "ootd글 좋아요 추가 혹은 제거 \n" +
            "                                          Parameter: nickname,ootdidx\n" +
            "                                          Response: 유저가 좋아요한 글 정보(nickname,ootdIdx)") // 성공
    public List<OotdLikeDTO> ootdLike(@Valid @RequestBody OotdLikeDTO ootdLikeDTO) {
        Ootd ootd = entityManager.find(Ootd.class, ootdLikeDTO.getOotdIdx());
        List<OotdLikeDTO> ootdLikeDTOList = null;
        if (ootd != null) {
            ootdLikeDTOList = ootdService.ootdLike(ootdLikeDTO);
        }
        return ootdLikeDTOList;
    }


    @GetMapping("/ootd/hashtag/{hashtag}") //
    @ApiOperation(value = "Ootd 해시태그기반 검색(해시태그 클릭) (O)", notes = "ootd상세글 혹은 검색에서 해시태그를 클릭 시 해당 해시태그가 포함된 글을 전부 리턴해준다.\n" +
            "                                                Parameter: hashtag\n" +
            "                                                Response: List<OotdMainDTO>")
    public List<OotdMainDTO> hashtagSearchByClick(@PathVariable String hashtag) {
        List<OotdMainDTO> ootdMainDTOList = ootdService.searchByHashtagClick(hashtag);
        return ootdMainDTOList;
    }

    @GetMapping("/ootd/hashtag/search/{hashtag}") //
    @ApiOperation(value = "Ootd 해시태그기반 검색(해시태그 입력) (O)", notes = "ootd상세글 혹은 검색에서 해시태그를 입력시 해당 해시태그가 포함된 글을 전부 리턴해준다.\n" +
            "                                                Parameter: hashtag\n" +
            "                                                Response: List<OotdMainDTO>")
    public List<OotdMainDTO> hashtagSearchByInput(@PathVariable String hashtag) {
        List<OotdMainDTO> ootdMainDTOList = ootdService.searchByHashtagInput(hashtag);
        return ootdMainDTOList;
    }

    @Transactional(rollbackOn = RuntimeException.class)
    @PostMapping("/ootd") // 내용, 닉네임 같은것 입력하면 ootd에만 들어가고 해시태그 안들어가는 오류발생
    @ApiOperation(value = "Ootd글 작성 (O)", notes = "Parameter: nickName, content, hashtag\n" +
            "                                        Response: success or fail")
    public String writeOotd(@Valid @RequestBody OotdWriteDTO ootdWriteDTO) {
        boolean ifSuccess = ootdService.writeOotd(ootdWriteDTO);
        if (ifSuccess) {
            return "success";
        } else
            return "fail";
    }

    @PostMapping("/ootd/reply")
    @ApiOperation(value = "Ootd글 댓글 작성 (O)", notes = "Parameter: idx(null),ootd_idx,nickname,content,depth,write_date(null),like(null),groupNum(null),count(0),flag(null) \n" +
            "                                       Response: 댓글 리스트 목록 전체")
    public List<OotdReplyDTO> ootdReplyWrite(@Valid @RequestBody OotdReplyDTO ootdreplyDTO) {
        List<OotdReplyDTO> ootdReplyDTOList = ootdService.writeReply(ootdreplyDTO);
        return ootdReplyDTOList;
    }

    @PutMapping("/ootd/reply")
    @Transactional
    @ApiOperation(value = "Ootd글 댓글 수정 (O)", notes = "Parameter: reply_idx,content\n" +
            "                                           Response: 수정한 댓글 정보")
    public List<OotdReplyDTO> ootdReplyUpdate(@Valid @RequestBody OotdReplyUpdateDTO ootdReplyUpdateDTO) {
        List<OotdReplyDTO> ootdReplyDTOList = ootdService.updateReply(ootdReplyUpdateDTO);
        return ootdReplyDTOList;
    }

    @DeleteMapping("/ootd/reply{idx}")
    @Transactional
    @ApiOperation(value = "Ootd글 댓글 삭제 (O)", notes = "Parameter: reply_idx\n" +
            "                                           Response: 삭제한(비활성) 댓글")
    public List<OotdReplyDTO> ootdReplyDelete(@PathVariable Long idx) {
        List<OotdReplyDTO> ootdReplyDTOList = ootdService.deleteReply(idx);
        return ootdReplyDTOList;
    }

    @Transactional
    @PutMapping("/ootd/reply/like/{reply_idx}/{nickname}")
    @ApiOperation(value = "Ootd 댓글에 대한 좋아요 설정 (O)", notes = "Parameter: reply_idx,nickname\n" +
            "                                                 Response: 해다 유저가 좋아요한 댓글 리스트")
    public List<OotdReplyDTO> ootdReplyLike(@PathVariable Long reply_idx, @PathVariable String nickname) {
        List<OotdReplyDTO> ootdReplyDTOList = ootdService.ootdReplyLike(reply_idx, nickname);
        return ootdReplyDTOList;
    }
}