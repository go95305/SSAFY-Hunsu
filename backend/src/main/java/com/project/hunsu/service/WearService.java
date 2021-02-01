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
    private final ReplyRepository replyRepository;
    private final VoteRepository voteRepository;
    private final VoteItemRepository voteItemRepository;
    private final VoteChoiceRepository voteChoiceRepository;
    private final ReplyLikeRepository replyLikeRepository;

    public WearService(ReplyLikeRepository replyLikeRepository, WearRepository wearRepository, UserRepository userRepository, ReplyRepository replyRepository, VoteRepository voteRepository, VoteItemRepository voteItemRepository, VoteChoiceRepository voteChoiceRepository) {
        this.wearRepository = wearRepository;
        this.userRepository = userRepository;
        this.replyRepository = replyRepository;
        this.voteRepository = voteRepository;
        this.voteItemRepository = voteItemRepository;
        this.voteChoiceRepository = voteChoiceRepository;
        this.replyLikeRepository = replyLikeRepository;
    }

    public List<WearMain> SortByRecent() {
        List<WearMain> wearMainList = new ArrayList<>();
        List<Wear> wearList;

        wearList = wearRepository.findWearByOrderByWriteDate();
        for (Wear wear : wearList) {
            WearMain wearMain = new WearMain();
            wearMain.setTitle(wear.getTitle());
            wearMain.setNickname(wear.getUser().getNickname());
            wearMain.setWear_idx(wear.getIdx());
            wearMain.setVoteActivated(wear.isVoteActivated());

            wearMainList.add(wearMain);
        }
        return wearMainList;
    }

    public void InsertWear(WearValue request) {
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

    public WearDetail DetailWear(long idx) {
        WearDetail wearDetail = new WearDetail();
        Wear wear = new Wear();

        wear = wearRepository.findWearByIdx(idx);

        wearDetail.setWear_idx(wear.getIdx());
        wearDetail.setTitle(wear.getTitle());
        wearDetail.setContent(wear.getContent());
        wearDetail.setNickname(wear.getUser().getNickname());
        wearDetail.setWrite_date(wear.getWriteDate());
        wearDetail.setVote_activated(wear.isVoteActivated());

        return wearDetail;
    }

    public List<VoteValue> VoteList(Long idx, String nickname) {
        List<VoteValue> voteValueList = new ArrayList<>();
        List<VoteItem> voteList;

        Wear wear = wearRepository.findWearByIdx(idx);
        Vote vote = voteRepository.findVoteByWear(wear);
        User user = userRepository.findUserByNickname(nickname);

        voteList = voteItemRepository.findVoteItemByVoteOrderByVote(vote);

        for (VoteItem voteItem : voteList) {
            VoteChoice voteChoice;
            voteChoice = voteChoiceRepository.findVoteChoiceByVoteItemAndUser(voteItem, user);
            VoteValue voteValue = new VoteValue();
            voteValue.setIdx(voteItem.getIdx());
            voteValue.setCount(voteItem.getCount());
            if(voteChoice != null)
                voteValue.setChoice(true);
            else
                voteValue.setChoice(false);

            voteValueList.add(voteValue);
        }

        return voteValueList;
    }

    public void DeleteWear(long idx) {
        Wear wear = wearRepository.findWearByIdx(idx);
        Vote vote = voteRepository.findVoteByWear(wear);
        List<VoteItem> voteItemList = voteItemRepository.findVoteItemByVoteOrderByVote(vote);
        List<Reply> replyList = replyRepository.findReplyByWearOrderByWriteDate(wear);

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
        Reply savedReply = replyRepository.save(reply);
        if(savedReply.getDepth() == 1)
            savedReply.setGroupNum(request.getGroupNum());
        else
            savedReply.setGroupNum(savedReply.getIdx());

        replyRepository.save(savedReply);
    }

    public void ModifyReply(ReplyDTO request) {
        Reply reply = replyRepository.findReplyByIdx(request.getIdx());
        reply.setContent(request.getContent());

        replyRepository.save(reply);
    }

    public void DeleteReply(Long idx) {
        Reply reply = replyRepository.findReplyByIdx(idx);

        if(reply.getDepth() == 0)
            replyRepository.deleteReplyByGroupNum(reply.getGroupNum());
        else
            replyRepository.deleteReplyByIdx(idx);
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
