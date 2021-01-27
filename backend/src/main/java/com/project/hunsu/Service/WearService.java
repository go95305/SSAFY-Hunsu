package com.project.hunsu.Service;

import com.project.hunsu.Dto.*;
import com.project.hunsu.Entity.*;
import com.project.hunsu.kakao.Repository.*;
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

    public WearService(WearRepository wearRepository, UserRepository userRepository, ReplyRepository replyRepository, VoteRepository voteRepository, VoteItemRepository voteItemRepository, VoteChoiceRepository voteChoiceRepository) {
        this.wearRepository = wearRepository;
        this.userRepository = userRepository;
        this.replyRepository = replyRepository;
        this.voteRepository = voteRepository;
        this.voteItemRepository = voteItemRepository;
        this.voteChoiceRepository = voteChoiceRepository;
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
        wear.setContent(request.getContent());
        if(request.getNum() > 0)
            wear.setVoteActivated(true);
        else
            wear.setVoteActivated(false);

        Wear savedWear = wearRepository.save(wear);

        if(request.getNum() > 0){
            Vote vote = new Vote();
            vote.setWear(savedWear);

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

        Vote vote = voteRepository.findVoteByWearIdx(idx);
        User user = userRepository.findUserByNickname(nickname);

        voteList = voteItemRepository.findVoteItemByWearIdxOrderByIdx(vote.getIdx());

        for (VoteItem voteItem : voteList) {
            VoteChoice voteChoice;
            voteChoice = voteChoiceRepository.findVoteChoicByVoteItemIdxAndUser(voteItem.getIdx(), user);
            /////수정 필요
            VoteValue voteValue = new VoteValue();
            voteValue.setIdx(voteItem.getIdx());
            voteValue.setCount(voteItem.getCount());
            if(nickname.equals(voteChoice))
                voteValue.setChoice(true);
            else
                voteValue.setChoice(false);

            voteValueList.add(voteValue);
        }

        return voteValueList;
    }

    public void DeleteWear(long idx) {
        wearRepository.deleteWearByIdx(idx);
    }

    public void InsertReply(ReplyValue request) {
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

        savedReply.setGroupNum(savedReply.getIdx());

        replyRepository.save(savedReply);
    }

    public void ModifyReply(ReplyValue request) {
        Reply reply = replyRepository.findReplyByIdx(request.getIdx());
        reply.setContent(request.getContent());

        replyRepository.save(reply);
    }

    public void DeleteReply(Long idx) {
        Reply reply = entityManager.find(Reply.class, idx);
        replyRepository.deleteReplyByIdx(idx);
    }

}
