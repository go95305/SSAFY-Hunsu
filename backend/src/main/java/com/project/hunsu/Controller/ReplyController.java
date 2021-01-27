package com.project.hunsu.Controller;

import com.project.hunsu.Entity.Reply;
import com.project.hunsu.kakao.Repository.ReplyRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReplyController {
    private final ReplyRepository replyRepository;

    public ReplyController(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @GetMapping("/reply/detail")
    public List<Reply> detailReply(){//나중에 리턴어떻게 할건지 생각해보자
        List<Reply> detail = replyRepository.findAll();
        return detail;
    }
}
