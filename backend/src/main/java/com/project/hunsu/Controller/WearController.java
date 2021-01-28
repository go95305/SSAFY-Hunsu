package com.project.hunsu.Controller;

import com.project.hunsu.Dto.*;
import com.project.hunsu.Entity.*;
import com.project.hunsu.Service.WearService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@RestController
public class WearController {
    private final WearService wearService;

    public WearController(WearService wearService) {
        this.wearService = wearService;
    }

    @PersistenceContext    // EntityManagerFactory가 DI 할 수 있도록 어노테이션 설정
    private EntityManager entityManager;

    //뭘 입을까 목록(최신순 정렬)
    @GetMapping("/wear")
    @ApiOperation(value = "뭘 입을까 메인")
    public List<WearMain> wearMain() {
        List<WearMain> wearList = wearService.SortByRecent();
        return wearList;
    }

    //뭘 입을까 작성
    //content, nickname, num, title 필요
    @PostMapping("/wear")
    @ApiOperation(value = "뭘 입을까 작성")
    public void insertWear(@RequestBody WearValue request) {
        wearService.InsertWear(request);
    }

    //뭘 입을까 디테일
    //wear_idx 필요
    @GetMapping("/wear/detail/{wear_idx}")
    @ApiOperation(value = "뭘 입을까 디테일")
    public WearDetail detailWear(@PathVariable Long wear_idx) {
        WearDetail wereDetail = wearService.DetailWear(wear_idx);
        return wereDetail;
    }

    //뭘 입을까 디테일에 붙은 vote
    //wear_idx, nickname 필요
    @GetMapping("/wear/detail/vote/{wear_idx}/{nickName}")
    @ApiOperation(value = "뭘 입을까 디테일 투표")
    public List<VoteValue> detailWearVote(@PathVariable Long wear_idx, @PathVariable String nickName) {
        List<VoteValue> voteValueList = wearService.VoteList(wear_idx, nickName);
        return voteValueList;
    }

    //뭘 입을까 삭제(cascade)&&미완
    //wear_idx 필요
    @DeleteMapping(value = "/wear/{wear_idx}")
    @Transactional
    @ApiOperation(value = "뭘 입을까 삭제")
    public void deleteWear(@PathVariable Long wear_idx) {
        wearService.DeleteWear(wear_idx);
    }

    //댓글 작성
    //content, depth, groupNum, nickname, wear_idx 필요
    @PostMapping(value = "/wear/reply")
    @Transactional
    @ApiOperation(value = "댓글 작성")
    public void insertReply(@RequestBody ReplyValue request) {
        wearService.InsertReply(request);
    }

    //댓글 수정
    //reply_idx, content 필요
    @PutMapping(value = "/wear/reply")
    @Transactional
    @ApiOperation(value = "댓글 수정")
    public void updateReply(@RequestBody ReplyValue request) {
        wearService.ModifyReply(request);
    }

    //댓글 삭제
    //reply_idx 필요
    @DeleteMapping(value = "/wear/reply/{idx}")
    @Transactional
    @ApiOperation(value = "댓글 삭제")
    public void deleteReply(@PathVariable Long idx) {
        wearService.DeleteReply(idx);
    }

    //투표
    //vote_item_idx, nickname 필요
    @DeleteMapping(value = "/wear/reply/vote/{vote_item_idx}/{nickname}")
    @Transactional
    @ApiOperation(value = "투표/투표취소")
    public void deleteReply(@PathVariable Long vote_item_idx, @PathVariable String nickname) {
        wearService.VoteReply(vote_item_idx, nickname);
    }

}
