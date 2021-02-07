package com.project.hunsu.service;

import com.project.hunsu.repository.*;
import com.project.hunsu.model.dto.*;
import com.project.hunsu.model.entity.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class WearService {

    @PersistenceContext
    EntityManager entityManager;
    private final WearRepository wearRepository;
    private final UserRepository userRepository;
    private final WearReplyRepository replyRepository;
    private final VoteRepository voteRepository;
    private final VoteItemRepository voteItemRepository;
    private final VoteChoiceRepository voteChoiceRepository;
    private final WearReplyLikeRepository replyLikeRepository;

    public WearService(WearReplyLikeRepository replyLikeRepository, WearRepository wearRepository, UserRepository userRepository, WearReplyRepository replyRepository, VoteRepository voteRepository, VoteItemRepository voteItemRepository, VoteChoiceRepository voteChoiceRepository) {
        this.wearRepository = wearRepository;
        this.userRepository = userRepository;
        this.replyRepository = replyRepository;
        this.voteRepository = voteRepository;
        this.voteItemRepository = voteItemRepository;
        this.voteChoiceRepository = voteChoiceRepository;
        this.replyLikeRepository = replyLikeRepository;
    }

    //return: title, nickname, wear_idx, voteActivated
    public List<WearMainDTO> sortByRecent() {
        List<WearMainDTO> wearMainDTOList = new ArrayList<>();
        List<Wear> wearList = wearRepository.findWearByFlagOrderByWriteDate(true);

        for (Wear wear : wearList) {
            WearMainDTO wearMainDTO = new WearMainDTO();
            wearMainDTO.setTitle(wear.getTitle());
            wearMainDTO.setNickname(wear.getUser().getNickname());
            wearMainDTO.setWear_idx(wear.getIdx());
            wearMainDTO.setVoteActivated(wear.isVoteActivated());

            wearMainDTOList.add(wearMainDTO);
        }

        return wearMainDTOList;
    }

    public void insertWear(WearDTO request) {
        Wear wear = new Wear();
        User user = userRepository.findUserByNickname(request.getNickname());

        wear.setUser(user);
        wear.setTitle(request.getTitle());
        wear.setContent(request.getContent());
        wear.setUser(user);
        if (request.getNum() > 0)
            wear.setVoteActivated(true);
        else
            wear.setVoteActivated(false);

        Wear savedWear = wearRepository.save(wear);

        if (request.getNum() > 0) {
            Vote vote = new Vote();
            vote.setWear(savedWear);
            vote.setEndTime(request.getEndtime());

            Vote savedVote = voteRepository.save(vote);
            //vote 생성

            for (int i = 0; i < request.getNum(); i++) {
                VoteItem voteItem = new VoteItem();
                voteItem.setVote(savedVote);

                voteItemRepository.save(voteItem);
                //vote 항목 생성
            }
        }
    }

    //return 값: wear_idx, title, content, nickname, write_date, vote_activated, List<Reply>(reply_idx, nickname, depth, writeDate, content, groupNum, count, like, flag), List<vote>(voteItem_idx, count, choice)
    public WearDetailDTO detailWear(long idx, String nickname) {
        WearDetailDTO wearDetailDTO = new WearDetailDTO();
        List<VoteItem> voteList;
        Wear wear = wearRepository.findWearByIdx(idx);
        Vote vote = voteRepository.findVoteByWear(wear);
        User user = userRepository.findUserByNickname(nickname);
        voteList = voteItemRepository.findVoteItemByVoteOrderByVote(vote);
        List<WearReplyDTO> replyDTOList = replyList(idx, nickname);
        List<VoteDTO> voteDTOList = new ArrayList<>();

        for (VoteItem voteItem : voteList) {
            VoteChoice voteChoice = voteChoiceRepository.findVoteChoiceByVoteItemAndUser(voteItem, user);

            VoteDTO voteDTO = new VoteDTO();
            voteDTO.setIdx(voteItem.getIdx());
            voteDTO.setCount(voteItem.getCount());
            if (voteChoice != null)
                voteDTO.setChoice(true);
            else
                voteDTO.setChoice(false);

            voteDTOList.add(voteDTO);
        }

        wearDetailDTO.setWear_idx(wear.getIdx());
        wearDetailDTO.setTitle(wear.getTitle());
        wearDetailDTO.setContent(wear.getContent());
        wearDetailDTO.setNickname(wear.getUser().getNickname());
        wearDetailDTO.setWrite_date(wear.getWriteDate());
        wearDetailDTO.setVote_activated(wear.isVoteActivated());
        wearDetailDTO.setReplyList(replyDTOList);
        wearDetailDTO.setVoteList(voteDTOList);

        return wearDetailDTO;
    }

    //return: voteItem_idx, count, choice
    public List<VoteDTO> voteList(Long idx, String nickname) {
        List<VoteDTO> voteDTOList = new ArrayList<>();
        List<VoteItem> voteList;

        VoteItem voteitem = voteItemRepository.findVoteItemByIdx(idx);
        Vote vote = voteitem.getVote();
        User user = userRepository.findUserByNickname(nickname);

        voteList = voteItemRepository.findVoteItemByVoteOrderByVote(vote);

        for (VoteItem voteItem : voteList) {
            VoteChoice voteChoice = voteChoiceRepository.findVoteChoiceByVoteItemAndUser(voteItem, user);

            VoteDTO voteDTO = new VoteDTO();
            voteDTO.setIdx(voteItem.getIdx());
            voteDTO.setCount(voteItem.getCount());
            if (voteChoice != null)
                voteDTO.setChoice(true);
            else
                voteDTO.setChoice(false);

            voteDTOList.add(voteDTO);
        }

        return voteDTOList;
    }

    public void deleteWear(long idx) {
        Wear wear = wearRepository.findWearByIdx(idx);

        wear.setFlag(false);

        wearRepository.save(wear);
    }

    //return: List<Reply>(reply_idx, nickname, depth, writeDate, content, groupNum, count, like, flag)
    public List<WearReplyDTO> insertReply(WearReplyDTO request) {
        WearReply wearReply = new WearReply();
        User user = userRepository.findUserByNickname(request.getNickname());
        Wear wear = wearRepository.findWearByIdx(request.getWear_idx());

        wearReply.setUser(user);
        wearReply.setWear(wear);
        wearReply.setDepth(request.getDepth());
        wearReply.setContent(request.getContent());
        WearReply savedReply = replyRepository.save(wearReply);
        if (savedReply.getDepth() == 1)
            savedReply.setGroupNum(request.getGroupNum());
        else
            savedReply.setGroupNum(savedReply.getIdx());

        replyRepository.save(savedReply);

        List<WearReplyDTO> replyDTOList = new ArrayList<>();

        replyDTOList = replyList(request.getWear_idx(), request.getNickname());

        return replyDTOList;

    }

    //return: List<Reply>(reply_idx, nickname, depth, writeDate, content, groupNum, count, like, flag)
    public List<WearReplyDTO> modifyReply(WearReplyDTO request) {
        WearReply reply = replyRepository.findReplyByIdx(request.getIdx());
        reply.setContent(request.getContent());

        replyRepository.save(reply);

        List<WearReplyDTO> replyDTOList = new ArrayList<>();

        replyDTOList = replyList(reply.getWear().getIdx(), request.getNickname());

        return replyDTOList;

    }

    //return: List<Reply>(reply_idx, nickname, depth, writeDate, content, groupNum, count, like, flag)
    public List<WearReplyDTO> deleteReply(Long idx) {
        WearReply reply = replyRepository.findReplyByIdx(idx);

        reply.setFlag(false);

        replyRepository.save(reply);

        List<WearReplyDTO> replyDTOList = new ArrayList<>();

        replyDTOList = replyList(reply.getWear().getIdx(), reply.getUser().getNickname());

        return replyDTOList;

    }

    public List<VoteDTO> voteChoice(Long idx, String nickname) {
        VoteItem voteItem = voteItemRepository.findVoteItemByIdx(idx);
        User user = userRepository.findUserByNickname(nickname);

        VoteChoice voteChoice = voteChoiceRepository.findVoteChoiceByVoteItemAndUser(voteItem, user);

        if (voteChoice != null) {
            if (voteChoice.getFlag()) {

                voteChoice.setFlag(false);

                voteItem.setCount(voteItem.getCount() - 1);

                voteItemRepository.save(voteItem);

            } else {

                voteChoice.setFlag(true);

                voteItem.setCount(voteItem.getCount() + 1);

                voteItemRepository.save(voteItem);

            }

        } else {
            VoteChoice createVoteChoice = new VoteChoice();

            createVoteChoice.setVoteItem(voteItem);
            createVoteChoice.setUser(user);

            voteChoiceRepository.save(createVoteChoice);

            voteItem.setCount(voteItem.getCount() + 1);

            voteItemRepository.save(voteItem);

        }

        List<VoteDTO> voteList = voteList(idx, nickname);

        return voteList;

    }

    public List<WearReplyDTO> replyLike(Long idx, String nickname) {
        WearReply reply = replyRepository.findReplyByIdx(idx);
        User user = userRepository.findUserByNickname(nickname);

        WearReplyLike replyLike = replyLikeRepository.findReplyLikeByReplyAndUser(reply, user);

        if (replyLike != null) {
            if (replyLike.getFlag()) {

                replyLike.setFlag(false);

                reply.setCount(reply.getCount() - 1);

                replyRepository.save(reply);

            } else {

                replyLike.setFlag(true);

                reply.setCount(reply.getCount() + 1);

                replyRepository.save(reply);

            }

        } else {
            WearReplyLike createReplyLike = new WearReplyLike();

            createReplyLike.setReply(reply);
            createReplyLike.setUser(user);

            replyLikeRepository.save(createReplyLike);

            reply.setCount(reply.getCount() + 1);

            replyRepository.save(reply);

        }

        List<WearReplyDTO> replyDTOList = new ArrayList<>();

        replyDTOList = replyList(reply.getWear().getIdx(), nickname);

        return replyDTOList;

    }

    public List<WearReplyDTO> replyList(Long idx, String nickname) {
        List<WearReplyDTO> replyDTOList = new ArrayList<>();

        Wear wear = wearRepository.findWearByIdx(idx);
        List<WearReply> replyList = replyRepository.findReplyByWearOrderByGroupNumDescWriteDateDesc(wear);
        User user = userRepository.findUserByNickname(nickname);

        for (WearReply reply : replyList) {
            WearReplyLike replyLike;
            replyLike = replyLikeRepository.findReplyLikeByReplyAndUser(reply, user);

            WearReplyDTO replyDTO = new WearReplyDTO();
            replyDTO.setIdx(reply.getIdx());
            replyDTO.setNickname(reply.getUser().getNickname());
            replyDTO.setDepth(reply.getDepth());
            replyDTO.setWrite_date(reply.getWriteDate());
            replyDTO.setContent(reply.getContent());
            replyDTO.setGroupNum(reply.getGroupNum());
            replyDTO.setCount(reply.getCount());
            if (replyLike != null) {
                if (replyLike.getFlag())
                    replyDTO.setLike(true);
                else
                    replyDTO.setLike(false);
            } else
                replyDTO.setLike(false);
            replyDTO.setFlag(reply.getFlag());

            replyDTOList.add(replyDTO);
        }

        return replyDTOList;
    }

}