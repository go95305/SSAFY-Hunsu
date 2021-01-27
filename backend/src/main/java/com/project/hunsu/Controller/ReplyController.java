package com.project.hunsu.Controller;

import com.project.hunsu.Dto.ReplyValue;
import com.project.hunsu.Service.ReplyService;
import com.project.hunsu.kakao.Repository.ReplyRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReplyController {
    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    //댓글 에 대한 좋아요/취소
    @GetMapping("/reply/wear/{wear_idx}/{nickname}")
    public List<ReplyValue> detailReply(@PathVariable Long wear_idx, @PathVariable String nickname){//나중에 리턴어떻게 할건지 생각해보자
        List<ReplyValue> replyList = replyService.ReplyList(wear_idx, nickname);

        return replyList;
    }

    //댓글 불러오기(보는사람이 댓글에 대한 좋아요 활성화)
}
