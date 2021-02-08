package com.project.hunsu.controller;

import com.project.hunsu.model.entity.Ootd;
import com.project.hunsu.model.entity.OotdLike;
import com.project.hunsu.model.entity.OotdReply;
import com.project.hunsu.model.entity.User;
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

    @GetMapping("/ootd/{sort}/{count}")  //ootd_idx, 닉네임, 글내용, 해시태그, 좋아요 개수
    @ApiOperation(value = "Ootd 메인페이지 (O)", notes = "0 혹은 1값을 보내면 \n" +
            "                                           0: 최신순으로 리턴 \n" +
            "                                           1: 인기순으로 리턴 \n" +
            "                                           count: 더보기 누른 횟수" +
            "                                           Parameter: 0 or 1/count(int)\n" +
            "                                           Response:  모든 ootd글을 정렬된 상태로 리턴해준다.")
    public List<OotdMainDTO> ootdSortedList(@PathVariable int sort, @PathVariable int count) {
        System.out.println(sort);
        List<OotdMainDTO> ootdMainDTOList = ootdService.SortByRecentOrPopularity(sort, count);
        return ootdMainDTOList;
    }

    @GetMapping("/ootd/detail/{ootdIdx}/{nickname}") // ootd_idx,content,count,is_updated,write_date,nickname
    @ApiOperation(value = "Ootd 상세페이지 (O)", notes = "ootd글에 대한 상세페이지, ootd메인페이지에서 특정 글을 클릭시\n" +
            "             해당 글에 대한 상세정보를 보여준다. 글의 ootdidx를 통해 연관된 hashtag,좋아요,댓글, 대댓글을 전부 리턴해준다.\n" +
            "             Parameter \n" +
            "             -ootdIdx(ootd글번호)\n" +
            "             -nickname(닉네임) \n" +
            "             Response \n" +
            "                        -ootdIdx(ootd글번호)\n" +
            "                        -content(해당 ootd글 내용)\n" +
            "                        -likeCount(ootd글 좋아요 개수)\n" +
            "                        -isUpdated(ootd글의 수정 여부)\n" +
            "                        -writeDate(글 작성 날짜)\n" +
            "                        -nickname\n" +
            "                        -hashtagList(해당 글의 해시태그들)\n" +
            "                        -ootdReplyList(해당 글의 댓글 리스트)\n" +
            "                        -likeChk(상세페이지에 들어간 유저가 해당글을 좋아요 눌렀는지 여부)")

    public OotdDetailDTO detailOotd(@PathVariable("ootdIdx") Long ootdIdx, @PathVariable String nickname) {
        OotdDetailDTO ootdDetailDTO = ootdService.SpecificOotd(ootdIdx, nickname);
        return ootdDetailDTO;
    }


    @PutMapping("/ootd")
    @Transactional
    @ApiOperation(value = "Ootd 글수정 (O)", notes = "Ootd상세 페이지에서 글 수정\n" +
            "                                       Parameter\n" +
            "                                        -ootdIdx(글 번호)\n" +
            "                                        -hashtag(새로 수정할 해시태그)\n" +
            "                                        -content(새로 수정할 글 내용) \n" +
            "                                       response\n" +
            "                                        -수정 성공 혹은 실패 여부(success or fail)")
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

    @DeleteMapping("/ootd/{ootdIdx}")
    @Transactional
    @ApiOperation(value = "Ootd 글삭제(비활성화) (O)", notes = "해당 글의 ootdIdx를 통해 해당글을 비활성화. 연관매핑이 되어있으므로 " +
            "연관된 테이블에 정보를 우선적으로 비활성화하고  해당 글을 비활성화\n" +
            "                                               Parameter\n" +
            "                                               -ootdIdx(글 번호)\n" +
            "                                               Response\n" +
            "                                                -글 비활성화 성공여부(success or fail)")
    public String deleteOotd(@PathVariable Long ootdIdx) {
        boolean flag = ootdService.deleteOotd(ootdIdx); //글 비활성화
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
            "                                          Parameter \n" +
            "                                           -nickname(닉네임)\n" +
            "                                           -ootdIdx(글 번호)\n" +
            "                                          Response\n" +
            "                                           -좋아요 추가 - true  좋아요 해제 - false") // 성공
    public Boolean ootdLike(@Valid @RequestBody OotdLikeDTO ootdLikeDTO) {
        Ootd ootd = entityManager.find(Ootd.class, ootdLikeDTO.getOotdIdx());
        List<OotdLikeDTO> ootdLikeDTOList = null;
        boolean chk = false;
        if (ootd != null) {
            chk = ootdService.ootdLike(ootdLikeDTO);
        }
        return chk;
    }


    @GetMapping("/ootd/hashtag/{hashtag}") //
    @ApiOperation(value = "Ootd 해시태그기반 검색(해시태그 클릭) (O)", notes = "ootd상세글 혹은 검색에서 해시태그를 클릭 시 해당 해시태그가 포함된 글을 전부 리턴해준다.\n" +
            "                                                Parameter\n" +
            "                                                -hashtag(검색할 해시태그)\n" +
            "                                                Response\n" +
            "                                                -List<OotdMainDTO>(해시태그가 포함된 모든 ootd글)")
    public List<OotdMainDTO> hashtagSearchByClick(@PathVariable String hashtag) {
        List<OotdMainDTO> ootdMainDTOList = ootdService.searchByHashtagClick(hashtag);
        return ootdMainDTOList;
    }

    @GetMapping("/ootd/hashtag/search/{hashtag}") //
    @ApiOperation(value = "Ootd 해시태그기반 검색(해시태그 입력) (O)", notes = "ootd상세글 혹은 검색에서 해시태그를 입력시 해당 해시태그가 포함된 글을 전부 리턴해준다.\n" +
            "                                                Parameter\n" +
            "                                                -hashtag(검색할 해시태그 문자열)\n" +
            "                                                Response\n" +
            "                                                -List<OotdMainDTO>(해시태그 문자열이 포함된 모든 ootd글)")
    public List<OotdMainDTO> hashtagSearchByInput(@PathVariable String hashtag) {
        List<OotdMainDTO> ootdMainDTOList = ootdService.searchByHashtagInput(hashtag);
        return ootdMainDTOList;
    }

    @Transactional(rollbackOn = RuntimeException.class)
    @PostMapping("/ootd") // 내용, 닉네임 같은것 입력하면 ootd에만 들어가고 해시태그 안들어가는 오류발생
    @ApiOperation(value = "Ootd글 작성 (O)", notes = "Parameter\n" +
            "                                                  -nickName(닉네임)\n" +
            "                                                  -content(작성할 내용)\n" +
            "                                                  -hashtag(작성할 해시태그)\n" +
            "                                        Response\n" +
            "                                                  -글 작성 성공여부(success or fail)")
    public String writeOotd(@Valid @RequestBody OotdWriteDTO ootdWriteDTO) {
        boolean ifSuccess = ootdService.writeOotd(ootdWriteDTO);
        if (ifSuccess) {
            return "success";
        } else
            return "fail";
    }

    @PostMapping("/ootd/reply")
    @ApiOperation(value = "Ootd글 댓글 작성 (O)", notes = "Parameter\n" +
            "                                                       -replyIdx(작성될 댓글 번호 - null)\n" +
            "                                                       -ootdIdx(댓글을 작성할 ootd글 번호)\n" +
            "                                                       -nickname(댓글 작성자 닉네임)\n" +
            "                                                       -content(댓글 내용)\n" +
            "                                                       -depth(댓글혹은대댓글여부 - null)\n" +
            "                                                       -write_date(작성날짜 - null)\n" +
            "                                                       -like(현재 유저가 좋아요 눌렀지의 여부 - null)\n" +
            "                                                       -groupNum(어느 글에 종속된 댓글인지의 그룹 - null)\n" +
            "                                                       -likeCount(댓글 좋아요 개수 - null)\n" +
            "                                                       -isDeleted(댓글 삭제 여부 - null) \n" +
            "                                       Response\n" +
            "                                                       -해당 글의 댓글 리스트 목록 전체")
    public List<OotdReplyDTO> ootdReplyWrite(@Valid @RequestBody OotdReplyDTO ootdreplyDTO) {
        List<OotdReplyDTO> ootdReplyDTOList = null;
        ootdReplyDTOList = ootdService.writeReply(ootdreplyDTO);
        return ootdReplyDTOList;
    }

    @PutMapping("/ootd/reply")
    @Transactional
    @ApiOperation(value = "Ootd글 댓글 수정 (O)", notes = "Parameter\n" +
            "                                                       -replyIdx(댓글 번호)\n" +
            "                                                       -content(댓글 내용)\n" +
            "                                           Response\n" +
            "                                                       -해당 글의 댓글 리스트 목록 전체")
    public List<OotdReplyDTO> ootdReplyUpdate(@Valid @RequestBody OotdReplyUpdateDTO ootdReplyUpdateDTO) {
        List<OotdReplyDTO> ootdReplyDTOList = ootdService.updateReply(ootdReplyUpdateDTO);
        return ootdReplyDTOList;
    }

    @DeleteMapping("/ootd/reply/{replyIdx}")
    @Transactional
    @ApiOperation(value = "Ootd글 댓글 삭제 (O)", notes = "Parameter\n" +
            "                                                     -replyIdx(댓글 번호)\n" +
            "                                            Response\n" +
            "                                                     -해당 글의 댓글 리스트 목록 전체")
    public List<OotdReplyDTO> ootdReplyDelete(@PathVariable Long replyIdx) {
        List<OotdReplyDTO> ootdReplyDTOList = ootdService.deleteReply(replyIdx);
        return ootdReplyDTOList;
    }

    @Transactional
    @PutMapping("/ootd/reply/like/{replyIdx}/{nickname}")
    @ApiOperation(value = "Ootd 댓글에 대한 좋아요 설정 (O)", notes = "Parameter\n" +
            "                                                           -replyIdx(댓글 번호)\n" +
            "                                                           -nickname(댓글 좋아요 누른 유저의 닉네임)\n" +
            "                                                      Response\n" +
            "                                                           -해당 글의 댓글 리스트 목록 전체")
    public List<OotdReplyDTO> ootdReplyLike(@PathVariable Long replyIdx, @PathVariable String nickname) {
        List<OotdReplyDTO> ootdReplyDTOList = ootdService.ootdReplyLike(replyIdx, nickname);
        return ootdReplyDTOList;
    }
}