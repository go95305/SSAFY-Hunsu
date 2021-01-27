package com.project.hunsu.Controller;

import com.project.hunsu.Entity.*;
import com.project.hunsu.dto.WearMain;
import com.project.hunsu.kakao.Repository.ReplyRepository;
import com.project.hunsu.kakao.Repository.WearRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WearController {

    private final WearRepository wearRepository;
    private final ReplyRepository replyRepository;

    public WearController(WearRepository wearRepository, ReplyRepository replyRepository) {
        this.wearRepository = wearRepository;
        this.replyRepository = replyRepository;
    }

    @PersistenceContext    // EntityManagerFactory가 DI 할 수 있도록 어노테이션 설정
    private EntityManager entityManager;

    //뭘 입을까 목록(최신순 정렬)
    @GetMapping("/wear")
    public List<WearMain> wearMain() {
        List<WearMain> wearMainList = new ArrayList<>();
        return wearRepository.findWearByOrderByWriteDate();
    }

    //뭘 입을까 작성(미완성)&&
    @PostMapping("/wear/{num}")
    public void insertWear(@PathVariable Integer num, @RequestBody Wear request) {
        Wear wear = new Wear();
        wear.setUser(request.getUser());
        wear.setTitle(request.getTitle());
        wear.setContent(request.getContent());
        wear.setContent(request.getContent());
        wearRepository.save(wear);

        if(num > 0){
            Vote vote = new Vote();
            vote.setWear();
            //vote 생성

            long idx = 0;//위 vote로 생성된 값의 idx 가져오기

            for (int i = 0; i < num; i++){
                VoteItem voteItem = new VoteItem();
                voteItem.setItem();
                //투표
            }
        }
    }

    //뭘 입을까 디테일(미완성)&&
    @GetMapping("/wear/detail/{idx}")
    public Wear detailWear(@PathVariable Long idx) {
        return wearRepository.findWearByIdx(idx);
    }

    //뭘 입을까 삭제(cascade)&&
    @DeleteMapping(value = "/wear/{idx}")
    @Transactional
    public void deleteWear(@PathVariable Long idx) {
        wearRepository.deleteWearByIdx(idx);
    }

    //댓글 작성(미완성)&&
    @PostMapping(value = "/wear/reply")
    @Transactional
    public void insertReply(@RequestBody Reply request) {
        Reply reply = new Reply();
        reply.setUser(request.getUser());
        reply.setWear(request.getWear());
        reply.setDepth(request.getDepth());
        reply.setContent(request.getContent());
        replyRepository.save(reply);
    }

    //댓글 수정(미완성)&&
    @PutMapping(value = "/wear/reply")
    @Transactional
    public void updateReply(@RequestBody Reply request) {
        Reply reply = replyRepository.getReplyByIdx(request.getIdx());
        reply.setContent(request.getContent());
        replyRepository.save(reply);
    }

    //댓글 삭제(미완성-뎃글 레포지토리 수정)&&
    @DeleteMapping(value = "/wear/reply/{idx}")
    @Transactional
    public void deleteReply(@PathVariable Long idx) {
        Reply reply = entityManager.find(Reply.class, idx);
        reply.setOotdActive(false);
        reply.setWearActive(false);
        replyRepository.deleteByIdx(idx);
    }

    //



}
