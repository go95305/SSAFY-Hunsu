package com.project.hunsu.controller;

import com.project.hunsu.service.WearService;
import com.project.hunsu.model.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@CrossOrigin(origins = {"*"})
public class WearController {
    private final WearService wearService;

    public WearController(WearService wearService) {
        this.wearService = wearService;
    }

    @PersistenceContext    // EntityManagerFactory가 DI 할 수 있도록 어노테이션 설정
    private EntityManager entityManager;

    //뭘 입을까 목록(최신순 정렬)
    @GetMapping("/wear")
    @ApiOperation(value = "뭘 입을까 메인 (O)", notes = "wear의 최신순 목록을 리턴해준다.\n" +
            "                                                Parameter: x\n" +
            "                                                Response: List<wear>(title, nickname, wear_idx, voteActivated)")
    public List<WearMainDTO> wearMain() {
        List<WearMainDTO> wearList = wearService.sortByRecent();
        return wearList;
    }

    //뭘 입을까 작성
    //content, nickname, num, title 필요
    @PostMapping("/wear")
    @ApiOperation(value = "뭘 입을까 작성 (O)", notes = "입력된 wear의 값들을 이용해 wear테이블에 값을 입력해준다.\n" +
            "                                                Parameter: content, nickname, num, title\n" +
            "                                                Response: x")
    public void insertWear(@RequestBody WearDTO request) {
        wearService.insertWear(request);
    }

    //뭘 입을까 디테일
    //wear_idx, nickname 필요
    @GetMapping("/wear/detail/{wear_idx}/{nickname}")
    @ApiOperation(value = "뭘 입을까 디테일 (O)", notes = "선택된 wear의 상세글, 댓글 목록, 투표 목록을 전부 리턴해준다.\n" +
            "                                                Parameter: wear_idx(path), nickname(path)\n" +
            "                                                Response: wear_idx, title, content, nickname, write_date, vote_activated,\n" +
            "                                                           List<reply>(reply_idx, nickname, depth, writeDate, content, groupNum, count, like, flag),\n" +
            "                                                           List<vote>(voteItem_idx, count, choice)")
    public WearDetailDTO detailWear(@PathVariable Long wear_idx, @PathVariable String nickname) {
        WearDetailDTO wereDetail = wearService.detailWear(wear_idx, nickname);
        return wereDetail;
    }

    //뭘 입을까 삭제(
    //wear_idx 필요
    @PutMapping(value = "/wear/{wear_idx}")
    @Transactional
    @ApiOperation(value = "뭘 입을까 삭제 (O)", notes = "wear_idx에 해당하는 글을 삭제(flag false로 변경)해준다.\n" +
            "                                                Parameter: wear_idx(path)\n" +
            "                                                Response: x")
    public void deleteWear(@PathVariable Long wear_idx) {
        wearService.deleteWear(wear_idx);
    }

    //댓글 작성
    //content, depth, groupNum, nickname, wear_idx 필요
    @PostMapping(value = "/wear/reply")
    @Transactional
    @ApiOperation(value = "댓글 작성 (O)", notes = "댓글의 값을 테이블에 입력해준다.\n" +
            "                                                Parameter: content, depth, groupNum, nickname, wear_idx\n" +
            "                                                Response: List<Reply>(reply_idx, nickname, depth, writeDate, content, groupNum, count, like, flag)")
    public List<WearReplyDTO> insertReply(@RequestBody WearReplyDTO request) {
        List<WearReplyDTO> replyDTOList = wearService.insertReply(request);

        return replyDTOList;
    }

    //댓글 수정
    //reply_idx, content 필요
    @PutMapping(value = "/wear/reply")
    @Transactional
    @ApiOperation(value = "댓글 수정 (O)", notes = "reply_idx에 해당하는 댓글을 수정해준다.\n" +
            "                                                Parameter: reply_idx, content\n" +
            "                                                Response: List<Reply>(reply_idx, nickname, depth, writeDate, content, groupNum, count, like, flag)")
    public List<WearReplyDTO> updateReply(@RequestBody WearReplyDTO request) {
        List<WearReplyDTO> replyDTOList = wearService.modifyReply(request);

        return replyDTOList;
    }

    //댓글 삭제
    //reply_idx 필요
    @PutMapping(value = "/wear/reply/{idx}")
    @Transactional
    @ApiOperation(value = "댓글 삭제 (O)", notes = "idx(reply_idx)에 해당하는 댓글을 삭제(flag를 false로 변경)해준다.\n" +
            "                                                Parameter: reply_idx\n" +
            "                                                Response: List<Reply>(reply_idx, nickname, depth, writeDate, content, groupNum, count, like, flag)")
    public List<WearReplyDTO> deleteReply(@PathVariable Long idx) {
        List<WearReplyDTO> replyDTOList = wearService.deleteReply(idx);

        return replyDTOList;
    }

    //댓글 에 대한 좋아요/취소
    //reply_idx, nickname 필요
    @PutMapping("/wear/reply/like/{reply_idx}/{nickname}")
    @Transactional
    @ApiOperation(value = "댓글 좋아요/좋아요취소 (O)", notes = "idx(reply_idx)의 댓글에 대한 좋아요를 추가/삭제 해준다.\n" +
            "                                                Parameter: reply_idx(path), nickname(path)\n" +
            "                                                Response: List<Reply>(reply_idx, nickname, depth, writeDate, content, groupNum, count, like, flag)")
    public List<WearReplyDTO> likeReply(@PathVariable Long reply_idx, @PathVariable String nickname) {
        List<WearReplyDTO> replyDTOList = wearService.replyLike(reply_idx, nickname);

        return replyDTOList;
    }

    //투표
    //vote_item_idx, nickname 필요
    @PutMapping(value = "/wear/reply/vote/{vote_item_idx}/{nickname}")
    @Transactional
    @ApiOperation(value = "투표/투표취소 (O)", notes = "idx(vote_item_idx)의 vote_item을 투표/투표취소 해준다.\n" +
            "                                                Parameter: vote_item_idx(path), nickname(path)\n" +
            "                                                Response: List<vote>(voteItem_idx, count, choice)")
    public List<VoteDTO> voteChoice(@PathVariable Long vote_item_idx, @PathVariable String nickname) {
        List<VoteDTO> voteDTOList = wearService.voteChoice(vote_item_idx, nickname);

        return voteDTOList;
    }

}