package com.project.hunsu.controller;

import com.project.hunsu.model.dto.ReplyDTO;
import com.project.hunsu.service.ReplyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@CrossOrigin(origins = {"*"})
public class ReplyController {
    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

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

}