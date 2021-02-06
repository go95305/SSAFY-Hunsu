package com.project.hunsu.service;

import com.project.hunsu.model.dto.*;
import com.project.hunsu.repository.*;
import com.project.hunsu.model.entity.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Service
public class OotdService {

    @PersistenceContext
    EntityManager entityManager;
    private final OotdRepository ootdRepository;
    private final HashtagRepository hashtagRepository;
    private final OotdLikeRepository ootdLikeRepository;
    private final OotdReplyRepository ootdReplyRepository;
    private final UserRepository userRepository;
    private final OotdReplyLikeRepository ootdReplyLikeRepository;

    public OotdService(OotdRepository ootdRepository, HashtagRepository hashtagRepository, OotdLikeRepository ootdLikeRepository, OotdReplyRepository ootdReplyRepository, UserRepository userRepository, OotdReplyLikeRepository ootdReplyLikeRepository) {
        this.ootdRepository = ootdRepository;
        this.hashtagRepository = hashtagRepository;
        this.ootdLikeRepository = ootdLikeRepository;
        this.ootdReplyRepository = ootdReplyRepository;
        this.userRepository = userRepository;
        this.ootdReplyLikeRepository = ootdReplyLikeRepository;
    }


    // 최신순 혹은 인기순으로 Ootd값을 정렬해서 가져오는 메소드 QueryDSL을 사용.
    public List<OotdMainDTO> SortByRecentOrPopularity(int sort) {
        List<OotdMainDTO> ootdMainDTOList = new ArrayList<>();
        List<Ootd> ootdList = new ArrayList<>();
        if (sort == 0) {
            ootdList = ootdRepository.findOotdByOrderByWriteDateDesc();
        } else {
            ootdList = ootdRepository.findOotdByOrderByCountDesc();
        }

        for (int i = 0; i < ootdList.size(); i++) {
            if (ootdList.get(i).getFlag()) {
                OotdMainDTO ootdMainDTO = new OotdMainDTO();
                ootdMainDTO.setOotdIdx(ootdList.get(i).getIdx());
                ootdMainDTO.setNickname(ootdList.get(i).getUser().getNickname());
                ootdMainDTO.setOotdContent(ootdList.get(i).getContent());
                List<Hashtag> hashtagList = hashtagRepository.findHashtagByOotdIdx(ootdList.get(i).getIdx());
                for (int j = 0; j < hashtagList.size(); j++) {
                    if (hashtagList.get(j).getFlag())
                        ootdMainDTO.addHashtag(hashtagList.get(j).getContent());
                }
                ootdMainDTO.setOotdLike(ootdList.get(i).getCount());
                ootdMainDTOList.add(ootdMainDTO);
            }
        }
        return ootdMainDTOList;
    }


    //Ootd 상세페이지 정보 가져오기
    public OotdDetailDTO SpecificOotd(Long ootdIdx, String nickname) {

        Ootd ootd = ootdRepository.findByIdx(ootdIdx);// OotdIdx를 통해 ootd글을 가져오기
        User user = userRepository.findUserByNickname(nickname);
        OotdDetailDTO ootdDetailDTO = null;
        if (user != null) {
            if (ootd != null) {
                ootdDetailDTO = new OotdDetailDTO();
                List<Hashtag> hashtagList = hashtagRepository.findHashtagByOotdIdx(ootdIdx);
                ootdDetailDTO.setOotdIdx(ootd.getIdx());
                ootdDetailDTO.setNickname(ootd.getUser().getNickname());
                ootdDetailDTO.setWriteDate(ootd.getWriteDate());
                ootdDetailDTO.setIsUpdated(ootd.getIsUpdated());
                ootdDetailDTO.setLikeCount(ootd.getCount());
                ootdDetailDTO.setContent(ootd.getContent());
                OotdLike ootdLike = ootdLikeRepository.findOotdLikeByOotdIdxAndUser(ootdIdx, user);
                if (ootdLike != null) {
                    if (ootdLike.getFlag()) {
                        ootdDetailDTO.setLikeChk(true);
                    } else {
                        ootdDetailDTO.setLikeChk(false);
                    }
                } else {
                    ootdDetailDTO.setLikeChk(false);
                }

                for (int i = 0; i < hashtagList.size(); i++) {
                    if (hashtagList.get(i).getFlag()) {
                        ootdDetailDTO.addHashtag(hashtagList.get(i).getContent());
                    }
                }
                //해당 ootd글의 댓글리스트 리턴
                List<OotdReply> ootdReplyList = ootdReplyRepository.findOotdReplyByOotdIdx(ootdIdx);
                for (int i = 0; i < ootdReplyList.size(); i++) {
                    OotdReplyDTO ootdreplDTO = new OotdReplyDTO();
                    ootdreplDTO.setReplyIdx(ootdReplyList.get(i).getIdx());
                    ootdreplDTO.setOotdIdx(ootdReplyList.get(i).getOotd().getIdx());
                    ootdreplDTO.setNickname(ootdReplyList.get(i).getUser().getNickname());
                    ootdreplDTO.setContent(ootdReplyList.get(i).getContent());
                    ootdreplDTO.setDepth(ootdReplyList.get(i).getDepth());
                    ootdreplDTO.setGroupNum(ootdReplyList.get(i).getGroupNum());
                    ootdreplDTO.setWrite_date(ootdReplyList.get(i).getWriteDate());
                    ootdreplDTO.setIsDeleted(ootdReplyList.get(i).getFlag());
                    ootdDetailDTO.addReply(ootdreplDTO);
                }

            }
        }
        return ootdDetailDTO;
    }

    //Ootd글 삭제
    public boolean deleteOotd(Long idx) {
        Ootd ootd = ootdRepository.findByIdx(idx);
        if (ootd != null) {
            //ootd와 관련된 연관관계 매핑 엔티티들과의 연결을 끊는다.(연관된 테이블의 레코드들을 전부 삭제) 그 이후에 Ootd테이블의 레코드 삭제
            List<OotdReply> ootdReply = ootdReplyRepository.findReplyByOotdIdx(idx);
            List<Hashtag> hashtagList = hashtagRepository.findHashtagByOotdIdx(idx);
            for (OotdReply repl : ootdReply
            ) {
                repl.setFlag(false); //해당 ootd글과 연관된 모든 댓글들은 전부 비활성화
                ootdReplyRepository.save(repl);
            }
            for (Hashtag hs : hashtagList) {
                hs.setFlag(false);
                hashtagRepository.save(hs);
            }

            Ootd ot = entityManager.find(Ootd.class, idx);
            ot.setFlag(false); // 해당 ootd 글 비활성화
            return true;
        } else
            return false;
    }

    //해시태그를 눌렀을때 해당해시태그가 포함된 모든 Ootd글들을 가져온다.
    public List<OotdMainDTO> searchByHashtagClick(String hashtag) {
        //우선 hashtag를 포함하는 모든 해시태그 테이블의 레코드를 가져온다.
        List<Hashtag> hashtagList = hashtagRepository.findByContent(hashtag); // 1. 우선 해시태그가 포함된 모든 hashtag 레코드 가져오기
        List<OotdMainDTO> ootdMainDTOList = hashtagSearch(hashtagList);
        return ootdMainDTOList;
    }

    //해시태그를 입력해서 검색
    public List<OotdMainDTO> searchByHashtagInput(String hashtag) {
        List<Hashtag> hashtagList = hashtagRepository.findByContentContaining(hashtag); // 1. 우선 해시태그가 포함된 모든 hashtag 레코드 가져오기
        List<OotdMainDTO> ootdMainDTOList = hashtagSearch(hashtagList);
        return ootdMainDTOList;
    }

    public List<OotdMainDTO> hashtagSearch(List<Hashtag> hashtagList) {
        List<OotdMainDTO> ootdMainDTOList = new ArrayList<>();
        for (int i = 0; i < hashtagList.size(); i++) {
            OotdMainDTO ootdMainDTO = new OotdMainDTO();
            Ootd ootd = ootdRepository.findByIdx(hashtagList.get(i).getOotd().getIdx());
            if (ootd.getFlag()) {
                ootdMainDTO.setOotdIdx(ootd.getIdx());
                ootdMainDTO.setNickname(ootd.getUser().getNickname());
                ootdMainDTO.setOotdContent(ootd.getContent());
                ootdMainDTO.setOotdLike(ootd.getCount());
                List<Hashtag> hashtagDTOList = hashtagRepository.findHashtagByOotdIdx(ootd.getIdx());
                for (int j = 0; j < hashtagDTOList.size(); j++) {
                    if (hashtagDTOList.get(j).getFlag())
                        ootdMainDTO.addHashtag(hashtagDTOList.get(j).getContent());
                }
                ootdMainDTOList.add(ootdMainDTO);
            }
        }
        return ootdMainDTOList;
    }

    //ootd글 생성
    public OotdDetailDTO writeOotd(OotdWriteDTO ootdWriteDTO) {
        OotdDetailDTO ootdDetailDTO = null;
        Ootd ootd = new Ootd();
        User user = userRepository.findUserByNickname(ootdWriteDTO.getNickName());
        //ootd글 새롭게 생성
        if (user != null) {
            ootd.setUser(user);
            ootd.setContent(ootdWriteDTO.getContent());
            ootd.setCount(0);
            ootd.setIsUpdated(false);
            Ootd ootdNew = ootdRepository.save(ootd);

            //만약 해시태그도 추가했다면 해시태그 테이블에도 레코드 추가해야한다.
            if (ootdWriteDTO.getHashtagList().size() != 0) {
//                TypedQuery<Ootd> query = entityManager.createQuery("select o from Ootd o where o.user.nickname = :nickname and o.content = :content order by o.idx asc ", Ootd.class);
//                query.setParameter("nickname", ootdWriteDTO.getNickName()).setParameter("content", ootdWriteDTO.getContent());
//                Ootd ot = query.getSingleResult();

                List<String> hashtagList = ootdWriteDTO.getHashtagList();

                for (int i = 0; i < hashtagList.size(); i++) {
                    Hashtag hashtag = new Hashtag();
                    hashtag.setContent(hashtagList.get(i));
                    hashtag.setOotd(ootdNew);
                    hashtagRepository.save(hashtag);
                }
            }
            List<Hashtag> hashtagList = hashtagRepository.findHashtagByOotdIdx(ootdNew.getIdx());
            ootdDetailDTO.setOotdIdx(ootdNew.getIdx());
            for(int i=0;i<hashtagList.size();i++){
                ootdDetailDTO.addHashtag(hashtagList.get(i).getContent());
            }
            ootdDetailDTO.setWriteDate(ootdNew.getWriteDate());
            ootdDetailDTO.setContent(ootdNew.getContent());
            ootdDetailDTO.setNickname(ootdNew.getUser().getNickname());
            ootdDetailDTO.setLikeChk(false);
            ootdDetailDTO.setLikeCount(0);
        }
        return ootdDetailDTO;


    }

    public void updateHashtag(OotdUpdateDTO ootdUpdateDTO) {
        // 1. 우선 기존의 해시태그부터 false시킨다.
        List<Hashtag> hashtagList = hashtagRepository.findHashtagByOotdIdx(ootdUpdateDTO.getOotdIdx());
        for (Hashtag hs : hashtagList) {
            hs.setFlag(false);
            hashtagRepository.save(hs);
        }
        // 2. 새로운 해시태그 입력
        for (int i = 0; i < ootdUpdateDTO.getHashtagList().size(); i++) {
            Hashtag hashtag = new Hashtag();
            Ootd ootd = ootdRepository.findByIdx(ootdUpdateDTO.getOotdIdx());
            hashtag.setContent(ootdUpdateDTO.getHashtagList().get(i));
            hashtag.setOotd(ootd);
            hashtagRepository.save(hashtag);
        }
        Ootd ootd = ootdRepository.findByIdx(ootdUpdateDTO.getOotdIdx());
        ootd.setContent(ootdUpdateDTO.getContent());
        ootd.setIsUpdated(true);
    }

    public List<OotdReplyDTO> writeReply(OotdReplyDTO ootdReplyDTO) {
        List<OotdReplyDTO> ootdReplyDTOList = new ArrayList<>();
        //1. 우선 댓글을 작성
        //2. 댓글 그룹은 일단 넣고 나서 업데이트!
        OotdReply ootdReply = new OotdReply();
        User user = new User();
        Ootd ootd = new Ootd();

        user = userRepository.findUserByNickname(ootdReplyDTO.getNickname());
        ootd = ootdRepository.findOotdByIdx(ootdReplyDTO.getOotdIdx());

        if (user != null && ootd != null) {
            ootdReply.setUser(user);
            ootdReply.setOotd(ootd);
            ootdReply.setDepth(ootdReplyDTO.getDepth());
            ootdReply.setContent(ootdReplyDTO.getContent());
            OotdReply savedReply = ootdReplyRepository.save(ootdReply);
            if (savedReply.getDepth() == 1)
                savedReply.setGroupNum(ootdReplyDTO.getGroupNum());
            else
                savedReply.setGroupNum(savedReply.getIdx());

            ootdReplyRepository.save(savedReply);

//        List<OotdReplyDTO> ootdreplyDTOList = new ArrayList<>();
//
//        replyDTOList = replyList(ootdReplyDTO.getOotd_idx(), ootdReplyDTO.getNickname());

            //2. 전체 댓글을 리턴
            List<OotdReply> ootdReplyList = ootdReplyRepository.findOotdReplyByOotdIdx(ootdReplyDTO.getOotdIdx());
            for (int i = 0; i < ootdReplyList.size(); i++) {
                OotdReplyDTO ootdreplDTO = new OotdReplyDTO();
                ootdreplDTO.setReplyIdx(ootdReplyList.get(i).getIdx());
                ootdreplDTO.setOotdIdx(ootdReplyList.get(i).getOotd().getIdx());
                ootdreplDTO.setNickname(ootdReplyList.get(i).getUser().getNickname());
                ootdreplDTO.setContent(ootdReplyList.get(i).getContent());
                ootdreplDTO.setDepth(ootdReplyList.get(i).getDepth());
                ootdreplDTO.setGroupNum(ootdReplyList.get(i).getGroupNum());
                ootdreplDTO.setWrite_date(ootdReplyList.get(i).getWriteDate());
                ootdreplDTO.setIsDeleted(ootdReplyList.get(i).getFlag());
                ootdReplyDTOList.add(ootdreplDTO);
            }
        }
        return ootdReplyDTOList;
    }

    public List<OotdReplyDTO> updateReply(OotdReplyUpdateDTO ootdReplyUpdateDTO) {
        // 우선 수정부터 해준다.
        List<OotdReplyDTO> ootdReplyDTOList = new ArrayList<>();
        OotdReply ootdReply = entityManager.find(OotdReply.class, ootdReplyUpdateDTO.getReplyIdx());
        ootdReply.setContent(ootdReplyUpdateDTO.getContent());
        // 내가 수정한 댓글 리스트
        ootdReplyDTOList = replyList(ootdReply.getOotd().getIdx(), ootdReply.getUser().getNickname());

        return ootdReplyDTOList;
    }

    public boolean ootdLike(OotdLikeDTO ootdLikeDTO) {
        boolean likeOrNot;
        //2. 안눌렀을 경우, 좋아요 추가

        //좋아요를 누른 ootd글을 가져온다.
        Ootd ootd = entityManager.find(Ootd.class, ootdLikeDTO.getOotdIdx());
        //좋아요를 누른 사용자 정보를 가져온다.
        User user = userRepository.findUserByNickname(ootdLikeDTO.getNickname());

        OotdLike ootdLike = ootdLikeRepository.findOotdLikeByOotdIdxAndUser(ootdLikeDTO.getOotdIdx(), user);
        if (ootdLike != null) {
            if (ootdLike.getFlag()) {
                ootdLike.setFlag(false);
                likeOrNot = false;
                ootd.setCount(ootd.getCount() - 1);
                ootdRepository.save(ootd);
            } else {
                ootdLike.setFlag(true);
                likeOrNot = true;
                ootd.setCount(ootd.getCount() + 1);
                ootdRepository.save(ootd);
            }
        } else {
            OotdLike otLike = new OotdLike();
            otLike.setFlag(true);
            likeOrNot = true;
            otLike.setUser(user);
            otLike.setOotd(ootd);
            ootdLikeRepository.save(otLike);
            ootd.setCount(ootd.getCount() + 1);
            ootdRepository.save(ootd);
        }
        //내가 좋아요한 사람들 글 목록 가져오기
        List<OotdLikeDTO> ootdLikeDTOList = new ArrayList<>();
        List<OotdLike> ootdLikeList = ootdLikeRepository.findOotdLikeByUserAndFlag(user, true);
        for (OotdLike like : ootdLikeList) {
            OotdLikeDTO otDTO = new OotdLikeDTO();
            otDTO.setNickname(user.getNickname());
            otDTO.setOotdIdx(like.getOotd().getIdx());
            ootdLikeDTOList.add(otDTO);
        }
        return likeOrNot;
    }

    public List<OotdReplyDTO> deleteReply(Long idx) {
        List<OotdReplyDTO> ootdReplyDTOList = new ArrayList<>();
        OotdReply ootdReply = ootdReplyRepository.findOotdReplyByIdx(idx);
        ootdReply.setFlag(false);
        ootdReplyDTOList = replyList(ootdReply.getOotd().getIdx(), ootdReply.getUser().getNickname());
        return ootdReplyDTOList;
    }

    public List<OotdReplyDTO> ootdReplyLike(Long idx, String nickname) { // 댓글번호, 누가 좋아요눌렀는지 닉네임
        OotdReply reply = ootdReplyRepository.findOotdReplyByIdx(idx);//해당 댓글 정보
        //28	ㅇㅇ	2	0	0	28	2021-02-05 09:07:53	22	4
        User user = userRepository.findUserByNickname(nickname);// 좋아요 누른 사람 정보
        //	2	1	1	1	1	1	1	1	go	1	1

        OotdReplyLike replyLike = ootdReplyLikeRepository.findOotdReplyLikeByOotdReplyAndUser(reply, user); //해당 댓글 좋아요 누른 사람의 댓글좋아요 정보

        if (replyLike != null) { //누가 좋아요했는지 정보가 있으면
            if (replyLike.getFlag()) { //

                replyLike.setFlag(false);

                reply.setCount(reply.getCount() - 1);

                ootdReplyRepository.save(reply);

            } else {

                replyLike.setFlag(true);

                reply.setCount(reply.getCount() + 1);

                ootdReplyRepository.save(reply);

            }

        } else {
            OotdReplyLike createReplyLike = new OotdReplyLike();

            createReplyLike.setOotdReply(reply);
            createReplyLike.setUser(user);

            ootdReplyLikeRepository.save(createReplyLike);

            reply.setCount(reply.getCount() + 1);

            ootdReplyRepository.save(reply);

        }

        List<OotdReplyDTO> replyDTOList = new ArrayList<>();

        replyDTOList = replyList(reply.getOotd().getIdx(), nickname);// 해당 댓글의 ootdIdx, 좋아요 누른 사람 닉네임

        return replyDTOList;

    }

    public List<OotdReplyDTO> replyList(Long idx, String nickname) {
        List<OotdReplyDTO> replyDTOList = new ArrayList<>();

        Ootd ootd = ootdRepository.findOotdByIdx(idx);//댓글이 포함된 ootd 글정보
        List<OotdReply> replyList = ootdReplyRepository.findOotdReplyByOotdOrderByWriteDate(ootd); //ootd글의 모든 댓글들
        User user = userRepository.findUserByNickname(nickname);//좋아요 누른 사람의 정보

        for (OotdReply reply : replyList) {//댓글들을 순차적으로 돌면서
            //현재좋아요 누른 유저가 해당 댓글에 좋아요 누른 데이터
            OotdReplyLike replyLike = ootdReplyLikeRepository.findOotdReplyLikeByOotdReplyAndUser(reply, user);

            OotdReplyDTO replyDTO = new OotdReplyDTO();
            replyDTO.setReplyIdx(reply.getIdx());
            replyDTO.setOotdIdx(reply.getOotd().getIdx());
            replyDTO.setNickname(reply.getUser().getNickname());
            replyDTO.setDepth(reply.getDepth());
            replyDTO.setWrite_date(reply.getWriteDate());
            replyDTO.setContent(reply.getContent());
            replyDTO.setGroupNum(reply.getGroupNum());
            replyDTO.setLikeCount(reply.getCount());
            if (replyLike != null) {
                if (replyLike.getFlag())//이미 좋아요면
                    replyDTO.setLike(true);//좋아요 취소
                else//좋아요 취소상태면
                    replyDTO.setLike(false);// 좋아요 추가
            } else
                replyDTO.setLike(false);
            replyDTO.setIsDeleted(reply.getFlag());

            replyDTOList.add(replyDTO);
        }

        return replyDTOList;
    }


}