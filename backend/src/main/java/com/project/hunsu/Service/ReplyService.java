package com.project.hunsu.Service;

import com.project.hunsu.Dto.ReplyValue;
import com.project.hunsu.Dto.WearDetail;
import com.project.hunsu.Dto.WearMain;
import com.project.hunsu.Dto.WearValue;
import com.project.hunsu.Entity.*;
import com.project.hunsu.kakao.Repository.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReplyService {

    @PersistenceContext
    EntityManager entityManager;
    private final ReplyRepository replyRepository;
    private final ReplyLikeRepository replyLikeRepository;
    private final UserRepository userRepository;

    public ReplyService(UserRepository userRepository, ReplyRepository replyRepository, ReplyLikeRepository replyLikeRepository) {
        this.userRepository = userRepository;
        this.replyRepository = replyRepository;
        this.replyLikeRepository = replyLikeRepository;
    }

    public List<ReplyValue> ReplyList(Long idx, String nickname) {
        List<ReplyValue> replyValueList = new ArrayList<>();
        List<Reply> replyList = new ArrayList<>();

        replyList = replyRepository.findReplyByWearIdxOrderByWriteDate(idx);
        User user = userRepository.findUserByNickname(nickname);

        for (Reply reply : replyList) {
            ReplyLike replyLike;
            replyLike = replyLikeRepository.findReplyLikeByReplyIdxAndUser(reply.getIdx(), user);
            /////수정 필요

            ReplyValue replyValue = new ReplyValue();
            replyValue.setIdx(reply.getIdx());
            replyValue.setNickname(reply.getUser().getNickname());
            replyValue.setDepth(reply.getDepth());
            replyValue.setWrite_date(reply.getWriteDate());
            replyValue.setContent(reply.getContent());
            replyValue.setGroupNum(reply.getGroupNum());
            if(nickname.equals(replyLike.getUser().getNickname()))
                replyValue.setLike(true);
            else
                replyValue.setLike(false);

            replyValueList.add(replyValue);
        }
        return replyValueList;
    }

}
