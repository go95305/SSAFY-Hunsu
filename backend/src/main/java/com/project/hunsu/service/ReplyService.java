package com.project.hunsu.service;

import com.project.hunsu.model.dto.ReplyDTO;
import com.project.hunsu.model.entity.Reply;
import com.project.hunsu.model.entity.ReplyLike;
import com.project.hunsu.model.entity.User;
import com.project.hunsu.model.entity.Wear;
import com.project.hunsu.repository.ReplyRepository;
import com.project.hunsu.repository.UserRepository;
import com.project.hunsu.repository.ReplyLikeRepository;
import com.project.hunsu.repository.WearRepository;
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
    private final WearRepository wearRepository;

    public ReplyService(WearRepository wearRepository, UserRepository userRepository, ReplyRepository replyRepository, ReplyLikeRepository replyLikeRepository) {
        this.userRepository = userRepository;
        this.replyRepository = replyRepository;
        this.replyLikeRepository = replyLikeRepository;
        this.wearRepository = wearRepository;
    }

    public List<ReplyDTO> ReplyList(Long idx, String nickname) {
        List<ReplyDTO> replyDTOList = new ArrayList<>();
        List<Reply> replyList = new ArrayList<>();
        Wear wear = wearRepository.findWearByIdx(idx);

        replyList = replyRepository.findReplyByWearOrderByWriteDate(wear);
        User user = userRepository.findUserByNickname(nickname);

        for (Reply reply : replyList) {
            ReplyLike replyLike;
            replyLike = replyLikeRepository.findReplyLikeByReplyAndUser(reply, user);
            /////수정 필요

            ReplyDTO replyDTO = new ReplyDTO();
            replyDTO.setIdx(reply.getIdx());
            replyDTO.setNickname(reply.getUser().getNickname());
            replyDTO.setDepth(reply.getDepth());
            replyDTO.setWrite_date(reply.getWriteDate());
            replyDTO.setContent(reply.getContent());
            replyDTO.setGroupNum(reply.getGroupNum());
            replyDTO.setCount(reply.getCount());
            if(replyLike != null)
                replyDTO.setLike(true);
            else
                replyDTO.setLike(false);

            replyDTOList.add(replyDTO);
        }
        return replyDTOList;
    }

    public void ReplyLike(Long idx, String nickname){
        Reply reply = replyRepository.findReplyByIdx(idx);
        User user = userRepository.findUserByNickname(nickname);

        ReplyLike replyLike = replyLikeRepository.findReplyLikeByReplyAndUser(reply, user);

        if(replyLike != null){
            replyLikeRepository.deleteReplyLikeByIdx(replyLike.getIdx());

            reply.setCount(reply.getCount() - 1);

            replyRepository.save(reply);

        } else {
            ReplyLike createReplyLike = new ReplyLike();

            createReplyLike.setReply(reply);
            createReplyLike.setUser(user);

            replyLikeRepository.save(createReplyLike);

            reply.setCount(reply.getCount() + 1);

            replyRepository.save(reply);

        }

    }

}
