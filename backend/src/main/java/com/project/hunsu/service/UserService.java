package com.project.hunsu.service;

import com.project.hunsu.model.dto.MyPageDetailDTO;
import com.project.hunsu.model.dto.ProfileDTO;
import com.project.hunsu.model.dto.VoteDTO;
import com.project.hunsu.model.dto.WearMainDTO;
import com.project.hunsu.model.entity.*;
import com.project.hunsu.repository.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    private final OotdLikeRepository ootdLikeRepository;
    private final OotdRepository ootdRepository;
    private final WearRepository wearRepository;
    private final OotdReplyLikeRepository ootdReplyLikeRepository;
    private final OotdReplyRepository ootdReplyRepository;
    private final VoteChoiceRepository voteChoiceRepository;
    private final WearReplyLikeRepository wearReplyLikeRepository;
    private final WearReplyRepository wearReplyRepository;
    @PersistenceContext
    EntityManager entityManager;

    public UserService(WearReplyRepository wearReplyRepository, WearReplyLikeRepository wearReplyLikeRepository, VoteChoiceRepository voteChoiceRepository, OotdReplyRepository ootdReplyRepository, OotdReplyLikeRepository ootdReplyLikeRepository, WearRepository wearRepository, OotdRepository ootdRepository, OotdLikeRepository ootdLikeRepository, UserRepository userRepository, FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
        this.ootdLikeRepository = ootdLikeRepository;
        this.ootdRepository = ootdRepository;
        this.wearRepository = wearRepository;
        this.ootdReplyLikeRepository = ootdReplyLikeRepository;
        this.ootdReplyRepository = ootdReplyRepository;
        this.voteChoiceRepository = voteChoiceRepository;
        this.wearReplyLikeRepository = wearReplyLikeRepository;
        this.wearReplyRepository = wearReplyRepository;
    }

    //return: mypageNickname(마이페이지의 대상 닉네임), mypage(내 마이페이지인지 여부), follow(팔로우 버튼 활성화 여부), ootd_like_list(ootd좋아요 리스트), ootd_list(ootd 리스트), follower_list(팔로우한 리스트),
    public MyPageDetailDTO mypage(String myNickName, String clickNickName) {
        MyPageDetailDTO myPageDetailDTO = new MyPageDetailDTO();
        User my = userRepository.findUserByNickname(myNickName);
        User user = userRepository.findUserByNickname(clickNickName);
        List<OotdLike> ootdLikeList = ootdLikeRepository.findOotdLikeByUser(user);
        List<Ootd> ootdList = ootdRepository.findOotdByUser(user);
        List<Follower> followerList = followerRepository.findFollowerByTargetIdAndFlag(user, true);
        List<Follower> followingList = followerRepository.findFollowerByMemIdAndFlag(user, true);
        Follower isActivated = followerRepository.findFollowerByMemIdAndTargetId(my, user);

        List<Long> ootdLikeIdxList = new ArrayList<>();
        List<Long> ootdIdxList = new ArrayList<>();
        List<String> followerNicknameList = new ArrayList<>();
        List<String> followingNicknameList = new ArrayList<>();

        myPageDetailDTO.setMypageNickname(clickNickName);
        if(myNickName.equals(clickNickName))
            myPageDetailDTO.setMypage(true);
        else{
            myPageDetailDTO.setMypage(false);
            if(isActivated != null)
                myPageDetailDTO.setFollow(true);
            else
                myPageDetailDTO.setFollow(false);
        }
        for (OotdLike ootdLike: ootdLikeList){
            Long idx = ootdLike.getIdx();

            ootdLikeIdxList.add(idx);
        }
        for (Ootd ootd: ootdList){
            Long idx = ootd.getIdx();

            ootdIdxList.add(idx);
        }
        for (Follower follower: followerList){
            String nickname = follower.getMemId().getNickname();

            followerNicknameList.add(nickname);
        }
        for (Follower follower: followingList){
            String nickname = follower.getTargetId().getNickname();

            followingNicknameList.add(nickname);
        }
        myPageDetailDTO.setOotd_like_list(ootdLikeIdxList);
        myPageDetailDTO.setOotd_list(ootdIdxList);
        myPageDetailDTO.setFollower_list(followerNicknameList);
        myPageDetailDTO.setFollowing_list(followingNicknameList);

        return myPageDetailDTO;

    }

    //return: mypageNickname(마이페이지의 대상 닉네임), mypage(내 마이페이지인지 여부), follow(팔로우 버튼 활성화 여부), ootd_like_list(ootd좋아요 리스트), ootd_list(ootd 리스트), follower_list(팔로우한 리스트),
    public ProfileDTO profileValue(String nickname) {
        ProfileDTO profileDTO = new ProfileDTO();

        User user = userRepository.findUserByNickname(nickname);

        profileDTO.setNickname(nickname);
        profileDTO.setGender(user.getGender());
        profileDTO.setHeight(user.getHeight());
        profileDTO.setSize(user.getSize());

        return profileDTO;

    }

    @Transactional
    public void profileModify(String nickname, ProfileDTO profileDTO) {
        User user = userRepository.findUserByNickname(nickname);

        user.setNickname(profileDTO.getNickname());
        user.setHeight(profileDTO.getHeight());
        user.setSize(profileDTO.getSize());

    }

    //return: isActivated(활성화: true, 비활성화: false)
    @Transactional
    public boolean follow(String myNickname, String yourNickname) {
        User member = userRepository.findUserByNickname(myNickname);
        User target = userRepository.findUserByNickname(yourNickname);
        Follower follower = followerRepository.findFollowerByMemIdAndTargetId(member, target);
        boolean isActivated = false;

        if(follower != null){
            if(follower.getFlag()){
                follower.setFlag(false);
                isActivated = false;
            } else if(!follower.getFlag()){
                follower.setFlag(true);
                isActivated = true;
            }
        } else if(follower == null){
            Follower createFollower = new Follower();

            createFollower.setMemId(member);
            createFollower.setTargetId(target);

            followerRepository.save(createFollower);

            isActivated = true;
        }

        return isActivated;

    }

    @Transactional
    public void userDelete(String nickname){
        User user = userRepository.findUserByNickname(nickname);

        user.setFlag(false);

        List<Ootd> ootdList = ootdRepository.findOotdByUser(user);
        List<Wear> wearList = wearRepository.findWearByUser(user);
        List<OotdLike> ootdLikeList = ootdLikeRepository.findOotdLikeByUser(user);
        List<OotdReplyLike> ootdReplyLikeList = ootdReplyLikeRepository.findOotdReplyLikeByUser(user);
        List<OotdReply> ootdReplyList = ootdReplyRepository.findOotdReplyByUser(user);
        List<VoteChoice> voteChoiceList = voteChoiceRepository.findVoteChoiceByUser(user);
        List<WearReplyLike> wearReplyLikeList = wearReplyLikeRepository.findReplyLikeByUser(user);
        List<WearReply> wearReplyList = wearReplyRepository.findReplyByUser(user);

        for (Ootd ootd : ootdList)
            ootd.setFlag(false);
        for (Wear wear : wearList)
            wear.setFlag(false);
        for (OotdLike ootdLike : ootdLikeList) {
            ootdLike.getOotd().setCount(ootdLike.getOotd().getCount() - 1);
            ootdLike.setFlag(false);
        }
        for (OotdReplyLike ootdReplyLike : ootdReplyLikeList) {
            ootdReplyLike.getOotdReply().setCount(ootdReplyLike.getOotdReply().getCount() - 1);
            ootdReplyLike.setFlag(false);
        }
        for (OotdReply ootdReply : ootdReplyList)
            ootdReply.setFlag(false);
        for (VoteChoice voteChoice : voteChoiceList) {
            voteChoice.getVoteItem().setCount(voteChoice.getVoteItem().getCount() - 1);
            voteChoice.setFlag(false);
        }
        for (WearReplyLike wearReplyLike : wearReplyLikeList) {
            wearReplyLike.getReply().setCount(wearReplyLike.getReply().getCount() - 1);
            wearReplyLike.setFlag(false);
        }
        for (WearReply wearReply : wearReplyList)
            wearReply.setFlag(false);

    }

}