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

    //뭘 입을까 작성(미완성)&&
    @PostMapping("/wear")
    @ApiOperation(value = "뭘 입을까 작성")
    public void insertWear(@RequestBody WearValue request) {
        wearService.InsertWear(request);
    }

    //뭘 입을까 디테일
    @GetMapping("/wear/detail/{idx}")
    @ApiOperation(value = "뭘 입을까 디테일")
    public WearDetail detailWear(@PathVariable Long idx) {
        WearDetail wereDetail = wearService.DetailWear(idx);
        return wereDetail;
    }

    //뭘 입을까 디테일에 붙은 vote
    @GetMapping("/wear/detail/vote/{wear_idx}/{nickName}")
    public List<VoteValue> detailWearVote(@PathVariable Long wear_idx, @PathVariable String nickName) {
        List<VoteValue> voteValueList = wearService.VoteList(wear_idx, nickName);
        return voteValueList;
    }

    //뭘 입을까 삭제(cascade)&&
    @DeleteMapping(value = "/wear/{idx}")
    @Transactional
    @ApiOperation(value = "뭘 입을까 삭제")
    public void deleteWear(@PathVariable Long idx) {
        wearService.DeleteWear(idx);
    }

    //댓글 작성
    @PostMapping(value = "/wear/reply")
    @Transactional
    public void insertReply(@RequestBody ReplyValue request) {
        wearService.InsertReply(request);
    }

    //댓글 수정
    @PutMapping(value = "/wear/reply")
    @Transactional
    public void updateReply(@RequestBody ReplyValue request) {
        wearService.ModifyReply(request);
    }

    //댓글 삭제(미완성-뎃글 레포지토리 수정)&&
    @DeleteMapping(value = "/wear/reply/{idx}")
    @Transactional
    public void deleteReply(@PathVariable Long idx) {
        wearService.DeleteReply(idx);
    }

    //투표

}
