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
    @ApiOperation(value = "뭘 입을까 메인 (O)")
    public List<WearMainDTO> wearMain() {
        List<WearMainDTO> wearList = wearService.sortByRecent();
        return wearList;
    }

    //뭘 입을까 작성
    //content, nickname, num, title 필요
    @PostMapping("/wear")
    @ApiOperation(value = "뭘 입을까 작성 (O)")
    public void insertWear(@RequestBody WearDTO request) {
        wearService.insertWear(request);
    }

    //뭘 입을까 디테일
    //wear_idx, nickname 필요
    @GetMapping("/wear/detail/{wear_idx}/{nickname}")
    @ApiOperation(value = "뭘 입을까 디테일 (O)")
    public WearDetailDTO detailWear(@PathVariable Long wear_idx, @PathVariable String nickname) {
        WearDetailDTO wereDetail = wearService.detailWear(wear_idx, nickname);
        return wereDetail;
    }

    //뭘 입을까 디테일에 붙은 vote
    //wear_idx, nickname 필요
    @GetMapping("/wear/detail/vote/{wear_idx}/{nickName}")
    @ApiOperation(value = "뭘 입을까 디테일 투표 (O)")
    public List<VoteDTO> detailWearVote(@PathVariable Long wear_idx, @PathVariable String nickName) {
        List<VoteDTO> voteDTOList = wearService.voteList(wear_idx, nickName);
        return voteDTOList;
    }

    //뭘 입을까 삭제(
    //wear_idx 필요
    @PutMapping(value = "/wear/{wear_idx}")
    @Transactional
    @ApiOperation(value = "뭘 입을까 삭제 (O)", notes = "hi")
    public void deleteWear(@PathVariable Long wear_idx) {
        wearService.deleteWear(wear_idx);
    }

    //댓글 작성
    //content, depth, groupNum, nickname, wear_idx 필요
    @PostMapping(value = "/wear/reply")
    @Transactional
    @ApiOperation(value = "댓글 작성 (O)")
    public List<WearReplyDTO> insertReply(@RequestBody WearReplyDTO request) {
        List<WearReplyDTO> replyDTOList = wearService.insertReply(request);

        return replyDTOList;
    }

    //댓글 수정
    //reply_idx, content 필요
    @PutMapping(value = "/wear/reply")
    @Transactional
    @ApiOperation(value = "댓글 수정 (X)")
    public List<WearReplyDTO> updateReply(@RequestBody WearReplyDTO request) {
        List<WearReplyDTO> replyDTOList = wearService.modifyReply(request);

        return replyDTOList;
    }

    //댓글 삭제
    //reply_idx 필요
    @PutMapping(value = "/wear/reply/{idx}")
    @Transactional
    @ApiOperation(value = "댓글 삭제 (X)")
    public List<WearReplyDTO> deleteReply(@PathVariable Long idx) {
        List<WearReplyDTO> replyDTOList = wearService.deleteReply(idx);

        return replyDTOList;
    }

    //댓글 에 대한 좋아요/취소
    //reply_idx, nickname 필요
    @PutMapping("/wear/reply/like/{reply_idx}/{nickname}")
    @Transactional
    @ApiOperation(value = "댓글 좋아요/좋아요취소 (~)")
    public List<WearReplyDTO> likeReply(@PathVariable Long reply_idx, @PathVariable String nickname) {
        List<WearReplyDTO> replyDTOList = wearService.replyLike(reply_idx, nickname);

        return replyDTOList;
    }

    //투표
    //vote_item_idx, nickname 필요
    @DeleteMapping(value = "/wear/reply/vote/{vote_item_idx}/{nickname}")
    @Transactional
    @ApiOperation(value = "투표/투표취소 (~)")
    public List<VoteDTO> voteChoice(@PathVariable Long vote_item_idx, @PathVariable String nickname) {
        List<VoteDTO> voteDTOList = wearService.voteChoice(vote_item_idx, nickname);

        return voteDTOList;
    }

}