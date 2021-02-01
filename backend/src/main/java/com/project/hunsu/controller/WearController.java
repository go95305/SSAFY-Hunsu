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
    public List<WearMain> wearMain() {
        List<WearMain> wearList = wearService.SortByRecent();
        return wearList;
    }

    //뭘 입을까 작성
    //content, nickname, num, title 필요
    @PostMapping("/wear")
    @ApiOperation(value = "뭘 입을까 작성 (O)")
    public void insertWear(@RequestBody WearValue request) {
        wearService.InsertWear(request);
    }

    //뭘 입을까 디테일
    //wear_idx 필요
    @GetMapping("/wear/detail/{wear_idx}")
    @ApiOperation(value = "뭘 입을까 디테일 (O)")
    public WearDetail detailWear(@PathVariable Long wear_idx) {
        WearDetail wereDetail = wearService.DetailWear(wear_idx);
        return wereDetail;
    }

    //뭘 입을까 디테일에 붙은 vote
    //wear_idx, nickname 필요
    @GetMapping("/wear/detail/vote/{wear_idx}/{nickName}")
    @ApiOperation(value = "뭘 입을까 디테일 투표 (O)")
    public List<VoteValue> detailWearVote(@PathVariable Long wear_idx, @PathVariable String nickName) {
        List<VoteValue> voteValueList = wearService.VoteList(wear_idx, nickName);
        return voteValueList;
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
