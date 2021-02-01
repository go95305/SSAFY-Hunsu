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

    ///////////////수정
    //댓글 불러오기(보는사람이 댓글에 대한 좋아요 활성화)
    @GetMapping("/reply/wear/{wear_idx}/{nickname}")
    @ApiOperation(value = "댓글 목록 (O)")
    public List<ReplyDTO> detailReply(@PathVariable Long wear_idx, @PathVariable String nickname){//나중에 리턴어떻게 할건지 생각해보자
        List<ReplyDTO> replyList = replyService.ReplyList(wear_idx, nickname);

        return replyList;
    }

    //댓글 에 대한 좋아요/취소        --reply_idx, nickname 필요
    @PutMapping("/reply/like/{reply_idx}/{nickname}")
    @Transactional
    @ApiOperation(value = "댓글 좋아요/좋아요취소 (~)")
    public void likeReply(@PathVariable Long reply_idx, @PathVariable String nickname) {
        replyService.ReplyLike(reply_idx, nickname);
    }
    /////////////////////

    //뭘 입을까 목록(최신순 정렬)
    @GetMapping("/wear")
    @ApiOperation(value = "뭘 입을까 메인 (O)")
    public List<WearMainDTO> wearMain() {
        List<WearMainDTO> wearList = wearService.SortByRecent();
        return wearList;
    }

    //뭘 입을까 작성
    //content, nickname, num, title 필요
    @PostMapping("/wear")
    @ApiOperation(value = "뭘 입을까 작성 (O)")
    public void insertWear(@RequestBody WearDTO request) {
        wearService.InsertWear(request);
    }

    //수정중
    //뭘 입을까 디테일
    //wear_idx, nickname 필요
    @GetMapping("/wear/detail/{wear_idx}/{nickname}")
    @ApiOperation(value = "뭘 입을까 디테일 (O)")
    public WearDetailDTO detailWear(@PathVariable Long wear_idx, @PathVariable String nickname) {
        WearDetailDTO wereDetail = wearService.DetailWear(wear_idx, nickname);
        return wereDetail;
    }

    //뭘 입을까 디테일에 붙은 vote
    //wear_idx, nickname 필요
    @GetMapping("/wear/detail/vote/{wear_idx}/{nickName}")
    @ApiOperation(value = "뭘 입을까 디테일 투표 (O)")
    public List<VoteDTO> detailWearVote(@PathVariable Long wear_idx, @PathVariable String nickName) {
        List<VoteDTO> voteDTOList = wearService.VoteList(wear_idx, nickName);
        return voteDTOList;
    }

    //뭘 입을까 삭제(cascade)&&미완
    //wear_idx 필요
    @DeleteMapping(value = "/wear/{wear_idx}")
    @Transactional
    @ApiOperation(value = "뭘 입을까 삭제 (O)", notes = "hi")
    public void deleteWear(@PathVariable Long wear_idx) {
        wearService.DeleteWear(wear_idx);
    }

    //댓글 작성
    //content, depth, groupNum, nickname, wear_idx 필요
    @PostMapping(value = "/wear/reply")
    @Transactional
    @ApiOperation(value = "댓글 작성 (O)")
    public void insertReply(@RequestBody ReplyDTO request) {
        wearService.InsertReply(request);
    }

    //댓글 수정
    //reply_idx, content 필요
    @PutMapping(value = "/wear/reply")
    @Transactional
    @ApiOperation(value = "댓글 수정 (X)")
    public void updateReply(@RequestBody ReplyDTO request) {
        wearService.ModifyReply(request);
    }

    //댓글 삭제
    //reply_idx 필요
    @DeleteMapping(value = "/wear/reply/{idx}")
    @Transactional
    @ApiOperation(value = "댓글 삭제 (X)")
    public void deleteReply(@PathVariable Long idx) {
        wearService.DeleteReply(idx);
    }

    //투표
    //vote_item_idx, nickname 필요
    @DeleteMapping(value = "/wear/reply/vote/{vote_item_idx}/{nickname}")
    @Transactional
    @ApiOperation(value = "투표/투표취소 (~)")
    public void deleteReply(@PathVariable Long vote_item_idx, @PathVariable String nickname) {
        wearService.VoteReply(vote_item_idx, nickname);
    }

}
