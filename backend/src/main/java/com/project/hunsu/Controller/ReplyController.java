package com.project.hunsu.Controller;

import com.project.hunsu.dto.Reply;
import com.project.hunsu.kakao.Repository.ReplyRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReplyController {
    private final ReplyRepository replyRepository;

    public ReplyController(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @GetMapping("/reply/detail")
    public Reply detailReply(@RequestParam Long ootd_idx){
        Reply detail = replyRepository.findAllByOotdIdx(ootd_idx);
        System.out.println(detail);
        return detail;
    }
}
