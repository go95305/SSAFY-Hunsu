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
    private final WearReplyRepository wearReplyRepository;
    private final VoteRepository voteRepository;
    private final VoteItemRepository voteItemRepository;
    private final VoteChoiceRepository voteChoiceRepository;
    private final OotdReplyLikeRepository replyLikeRepository;

    public WearService(OotdReplyLikeRepository replyLikeRepository, WearRepository wearRepository, UserRepository userRepository, WearReplyRepository wearReplyRepository, VoteRepository voteRepository, VoteItemRepository voteItemRepository, VoteChoiceRepository voteChoiceRepository) {
        this.wearRepository = wearRepository;
        this.userRepository = userRepository;
        this.wearReplyRepository = wearReplyRepository;
        this.voteRepository = voteRepository;
        this.voteItemRepository = voteItemRepository;
        this.voteChoiceRepository = voteChoiceRepository;
        this.replyLikeRepository = replyLikeRepository;
    }

    public List<WearMainDTO> SortByRecent() {
        List<WearMainDTO> wearMainDTOList = new ArrayList<>();
        List<Wear> wearList;

        wearList = wearRepository.findWearByOrderByWriteDate();
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

    public void InsertWear(WearDTO request) {
        Wear wear = new Wear();
        User user = new User();

        user = userRepository.findUserByNickname(request.getNickname());


        wear.setUser(user);
        wear.setTitle(request.getTitle());
        wear.setContent(request.getContent());
        wear.setUser(user);
        if(request.getNum() > 0)
            wear.setVoteActivated(true);
        else
            wear.setVoteActivated(false);

        Wear savedWear = wearRepository.save(wear);

        if(request.getNum() > 0){
            Vote vote = new Vote();
            vote.setWear(savedWear);
            vote.setEndTime(request.getEndtime());
            //수정 필요

            Vote savedVote = voteRepository.save(vote);
            //vote 생성

            for (int i = 0; i < request.getNum(); i++){
                VoteItem voteItem = new VoteItem();
                voteItem.setVote(savedVote);

                voteItemRepository.save(voteItem);
                //vote 항목 생성
            }
        }
    }

    //return 값: wear_idx, title, content, nickname, write_date, vote_activated, List<Reply>(
    public WearDetailDTO DetailWear(long idx, String nickname) {
        WearDetailDTO wearDetailDTO = new WearDetailDTO();
        Wear wear = new Wear();

        wear = wearRepository.findWearByIdx(idx);
        List<Reply> replyList = wearReplyRepository.findReplyByWearIdxAndIsActivatedOrderByWriteDate(wear, wear.getIsActivated());

        wearDetailDTO.setWear_idx(wear.getIdx());
        wearDetailDTO.setTitle(wear.getTitle());
        wearDetailDTO.setContent(wear.getContent());
        wearDetailDTO.setNickname(wear.getUser().getNickname());
        wearDetailDTO.setWrite_date(wear.getWriteDate());
        wearDetailDTO.setVote_activated(wear.isVoteActivated());
        wearDetailDTO.setReplyList(replyList);

        return wearDetailDTO;
    }

    public List<VoteDTO> VoteList(Long idx, String nickname) {
        List<VoteDTO> voteDTOList = new ArrayList<>();
        List<VoteItem> voteList;

        Wear wear = wearRepository.findWearByIdx(idx);
        Vote vote = voteRepository.findVoteByWear(wear);
        User user = userRepository.findUserByNickname(nickname);

        voteList = voteItemRepository.findVoteItemByVoteOrderByVote(vote);

        for (VoteItem voteItem : voteList) {
            VoteChoice voteChoice;
            voteChoice = voteChoiceRepository.findVoteChoiceByVoteItemAndUser(voteItem, user);
            VoteDTO voteDTO = new VoteDTO();
            voteDTO.setIdx(voteItem.getIdx());
            voteDTO.setCount(voteItem.getCount());
            if(voteChoice != null)
                voteDTO.setChoice(true);
            else
                voteDTO.setChoice(false);

            voteDTOList.add(voteDTO);
        }

        return voteDTOList;
    }

    public void DeleteWear(long idx) {
        Wear wear = wearRepository.findWearByIdx(idx);
        Vote vote = voteRepository.findVoteByWear(wear);
        List<VoteItem> voteItemList = voteItemRepository.findVoteItemByVoteOrderByVote(vote);
        List<Reply> replyList = wearReplyRepository.findReplyByWearOrderByWriteDate(wear);

        for (VoteItem voteItem : voteItemList)
            voteChoiceRepository.deleteVoteChoiceByVoteItem(voteItem);

//        for (Reply reply : replyList)
//            replyLikeRepository.deleteReplyLikeByIdx(reply.getIdx());

        voteItemRepository.deleteVoteItemByVote(vote);
        voteRepository.deleteVoteByWear(wear);
//        replyLikeRepository.deleteReplyLikeByIdx();

        wearRepository.deleteWearByIdx(idx);
    }

    public void InsertReply(ReplyDTO request) {
        Reply reply = new Reply();
        User user = new User();
        Wear wear = new Wear();

        user = userRepository.findUserByNickname(request.getNickname());
        wear = wearRepository.findWearByIdx(request.getWear_idx());

        reply.setUser(user);
        reply.setWear(wear);
        reply.setDepth(request.getDepth());
        reply.setContent(request.getContent());
        Reply savedReply = wearReplyRepository.save(reply);
        if(savedReply.getDepth() == 1)
            savedReply.setGroupNum(request.getGroupNum());
        else
            savedReply.setGroupNum(savedReply.getIdx());

        wearReplyRepository.save(savedReply);
    }

    public void ModifyReply(ReplyDTO request) {
        Reply reply = wearReplyRepository.findReplyByIdx(request.getIdx());
        reply.setContent(request.getContent());

        wearReplyRepository.save(reply);
    }

    public void DeleteReply(Long idx) {
        Reply reply = wearReplyRepository.findReplyByIdx(idx);

        if(reply.getDepth() == 0)
            wearReplyRepository.deleteReplyByGroupNum(reply.getGroupNum());
        else
            wearReplyRepository.deleteReplyByIdx(idx);
    }

    public void VoteReply(Long idx, String nickname){
        VoteItem voteItem = voteItemRepository.findVoteItemByIdx(idx);
        User user = userRepository.findUserByNickname(nickname);

        VoteChoice voteChoice = voteChoiceRepository.findVoteChoiceByVoteItemAndUser(voteItem, user);

        if(voteChoice != null) {
            voteChoiceRepository.deleteVoteChoiceByIdx(voteChoice.getIdx());

            voteItem.setCount(voteItem.getCount() - 1);

            voteItemRepository.save(voteItem);

        } else {
            VoteChoice createVoteChoice = new VoteChoice();

            createVoteChoice.setVoteItem(voteItem);
            createVoteChoice.setUser(user);

            voteChoiceRepository.save(createVoteChoice);

            voteItem.setCount(voteItem.getCount() + 1);

            voteItemRepository.save(voteItem);

        }

    }

}
