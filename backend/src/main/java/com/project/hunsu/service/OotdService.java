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
            ootdList = ootdRepository.findOotdByOrderByWriteDate();
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
                    ootdMainDTO.addHashtag(hashtagList.get(j).getContent());
                }
                ootdMainDTO.setOotdLike(ootdList.get(i).getCount());
                ootdMainDTOList.add(ootdMainDTO);
            }
        }
        return ootdMainDTOList;
    }


    //Ootd 상세페이지 정보 가져오기 //수정해
    public OotdDetailDTO SpecificOotd(Long ootdIdx) { //NickName, 작성일자, 수정 여부, 사진, 좋아요 카운트, style 해시태그 리스트, product 해시태그 리스트, 댓글, 대댓글
        Ootd ootd = ootdRepository.findByIdx(ootdIdx);// OotdIdx를 통해 ootd글을 가져오기
        List<Hashtag> hashtagList = hashtagRepository.findHashtagByOotdIdx(ootdIdx);
        List<OotdReply> ootdReplyList = ootdReplyRepository.findOotdReplyByOotdIdx(ootdIdx);
        OotdDetailDTO ootdDetailDTO = new OotdDetailDTO();
        ootdDetailDTO.setOotdIdx(ootd.getIdx());
        ootdDetailDTO.setNickname(ootd.getUser().getNickname());
        ootdDetailDTO.setWriteDate(ootd.getWriteDate());
        ootdDetailDTO.setIsUpdated(ootd.getIsUpdated());
        ootdDetailDTO.setCount(ootd.getCount());
        ootdDetailDTO.setContent(ootd.getContent());
        for (int i = 0; i < hashtagList.size(); i++) {
            ootdDetailDTO.addHashtag(hashtagList.get(i).getContent());
        }
        //댓글리스트도 리턴
        for (int i = 0; i < ootdReplyList.size(); i++) {
            OotdReplyDTO ootdReplyDTO = new OotdReplyDTO();
            ootdReplyDTO.setIdx(ootdReplyList.get(i).getIdx());
            ootdReplyDTO.setOotd_idx(ootdReplyList.get(i).getOotd().getIdx());
            ootdReplyDTO.setNickname(ootdReplyList.get(i).getUser().getNickname());
            ootdReplyDTO.setContent(ootdReplyList.get(i).getContent());
            ootdReplyDTO.setDepth(ootdReplyList.get(i).getDepth());
            ootdReplyDTO.setWrite_date(ootdReplyList.get(i).getWriteDate());
            ootdReplyDTO.setCount(ootdReplyList.get(i).getCount());
            ootdReplyDTO.setGroupNum(ootdReplyList.get(i).getGroupNum());
            ootdReplyDTO.setFlag(ootdReplyList.get(i).getFlag());
            ootdDetailDTO.addReply(ootdReplyDTO);
        }
        return ootdDetailDTO;
    }

    //Ootd글 삭제
    public void deleteOotd(Long idx) {
        //ootd와 관련된 연관관계 매핑 엔티티들과의 연결을 끊는다.(연관된 테이블의 레코드들을 전부 삭제) 그 이후에 Ootd테이블의 레코드 삭제
        List<OotdReply> ootdReply = ootdReplyRepository.findReplyByOotdIdx(idx);
        for (OotdReply repl : ootdReply
        ) {
            repl.setFlag(false); //해당 ootd글과 연관된 모든 댓글들은 전부 비활성화
        }

        Ootd ot = entityManager.find(Ootd.class, idx);
        ot.setFlag(false); // 해당 ootd 글 비활성화
    }

    //검색창에서 해시태그를 검색하거나 해시태그를 눌렀을때 해당해시태그가 포함된 모든 Ootd글들을 가져온다.
    public List<OotdMainDTO> searchByHashtag(String hashtag) {
        //우선 hashtag를 포함하는 모든 해시태그 테이블의 레코드를 가져온다.
        List<Hashtag> hashtagList = hashtagRepository.findByContent(hashtag); // 1. 우선 해시태그가 포함된 모든 hashtag 레코드 가져오기
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
                    ootdMainDTO.addHashtag(hashtagDTOList.get(j).getContent());
                }
                ootdMainDTOList.add(ootdMainDTO);
            }
        }
        // 2. 각 레코드들의 ootdidx로 ootd글 가져오기
        // 3. mainDTO에 저장
        // 4. 동일한 ootd글이면 넘어간다.
        return ootdMainDTOList;
    }

    //ootd글 생성
    public void writeOotd(OotdWriteDTO ootdWriteDTO) {
        Ootd ootd = new Ootd();
        User user = userRepository.findUserByNickname(ootdWriteDTO.getNickName());
        //ootd글 새롭게 생성
        ootd.setUser(user);
        ootd.setContent(ootdWriteDTO.getContent());
        ootd.setCount(0);
        ootd.setIsUpdated(false);
        entityManager.persist(ootd);

        //만약 해시태그도 추가했다면 해시태그 테이블에도 레코드 추가해야한다.
        if (ootdWriteDTO.getHashtagList().size() != 0) {
            TypedQuery<Ootd> query = entityManager.createQuery("select o from Ootd o where o.user.nickname = :nickname and o.content = :content order by o.idx asc ", Ootd.class);
            query.setParameter("nickname", ootdWriteDTO.getNickName()).setParameter("content", ootdWriteDTO.getContent());
            Ootd ot = query.getSingleResult();

            List<String> hashtagList = ootdWriteDTO.getHashtagList();

            for (int i = 0; i < hashtagList.size(); i++) {
                Hashtag hashtag = new Hashtag();
                hashtag.setContent(hashtagList.get(i));
                hashtag.setOotd(ot);
                hashtagRepository.save(hashtag);
            }
        }


    }

    public void updateHashtag(OotdUpdateDTO ootdUpdateDTO) {
        // 1. 우선 기존의 해시태그부터 false시킨다.
        List<Hashtag> hashtagList = hashtagRepository.findHashtagByOotdIdx(ootdUpdateDTO.getOotdIdx());
        for (Hashtag hs : hashtagList) {
            hs.setFlag(true);
        }
        // 2. 새로운 해시태그 입력
        for (int i = 0; i < ootdUpdateDTO.getHashtagList().size(); i++) {
            Hashtag hashtag = new Hashtag();
            Ootd ootd = ootdRepository.findByIdx(ootdUpdateDTO.getOotdIdx());
            hashtag.setContent(ootdUpdateDTO.getHashtagList().get(i));
            hashtag.setOotd(ootd);
            hashtagRepository.save(hashtag);
        }
    }

    public List<OotdReplyDTO> writeReply(OotdReplyDTO ootdReplyDTO) {
        List<OotdReplyDTO> replyDTOList = new ArrayList<>();
        //1. 우선 댓글을 작성
        //2. 댓글 그룹은 일단 넣고 나서 업데이트!
        OotdReply ootdReply = new OotdReply();
        User user = new User();
        Ootd ootd = new Ootd();

        user = userRepository.findUserByNickname(ootdReplyDTO.getNickname());
        ootd = ootdRepository.findOotdByIdx(ootdReplyDTO.getOotd_idx());

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
        ootdReplyRepository.save(savedReply);

//        List<OotdReplyDTO> ootdreplyDTOList = new ArrayList<>();
//
//        replyDTOList = replyList(ootdReplyDTO.getOotd_idx(), ootdReplyDTO.getNickname());

        //2. 전체 댓글을 리턴
        List<OotdReply> ootdReplyList = ootdReplyRepository.findOotdReplyByOrderByWriteDate();
        for (int i = 0; i < ootdReplyList.size(); i++) {
            OotdReplyDTO ootdreplDTO = new OotdReplyDTO();
            ootdreplDTO.setIdx(ootdReplyList.get(i).getIdx());
            ootdreplDTO.setOotd_idx(ootdReplyList.get(i).getOotd().getIdx());
            ootdreplDTO.setNickname(ootdReplyList.get(i).getUser().getNickname());
            ootdreplDTO.setContent(ootdReplyList.get(i).getContent());
            ootdreplDTO.setDepth(ootdReplyList.get(i).getDepth());
            ootdreplDTO.setGroupNum(ootdReplyList.get(i).getGroupNum());
            ootdreplDTO.setWrite_date(ootdReplyList.get(i).getWriteDate());
            ootdreplDTO.setFlag(ootdReplyList.get(i).getFlag());
            replyDTOList.add(ootdreplDTO);
        }
//        return ootdreplyDTOList;
        return replyDTOList;
    }

    public List<OotdReplyDTO> updateReply(OotdReplyUpdateDTO ootdReplyUpdateDTO) {
        // 우선 수정부터 해준다.
        List<OotdReplyDTO> ootdReplyDTOList = new ArrayList<>();
        OotdReply ootdReply = entityManager.find(OotdReply.class, ootdReplyUpdateDTO.getReplyIdx());
        ootdReply.setContent(ootdReplyUpdateDTO.getContent());
        // 전체 댓글 리스트 리턴
        List<OotdReplyDTO> replyDTOList = new ArrayList<>();
        replyDTOList = replyList(ootdReply.getOotd().getIdx(), ootdReply.getUser().getNickname());
//        List<OotdReply> ootdReplyList = ootdReplyRepository.findOotdReplyByOrderByWriteDate();
//        for (int i = 0; i < ootdReplyList.size(); i++) {
//            OotdReplyDTO ootdreplDTO = new OotdReplyDTO();
//            ootdreplDTO.setIdx(ootdReplyList.get(i).getIdx());
//            ootdreplDTO.setOotd_idx(ootdReplyList.get(i).getOotd().getIdx());
//            ootdreplDTO.setNickname(ootdReplyList.get(i).getUser().getNickname());
//            ootdreplDTO.setContent(ootdReplyList.get(i).getContent());
//            ootdreplDTO.setDepth(ootdReplyList.get(i).getDepth());
//            ootdreplDTO.setGroupNum(ootdReplyList.get(i).getGroupNum());
//            ootdreplDTO.setWrite_date(ootdReplyList.get(i).getWriteDate());
//            ootdreplDTO.setFlag(ootdReplyList.get(i).getFlag());
//            ootdReplyDTOList.add(ootdreplDTO);
//        }
        return ootdReplyDTOList;
    }

    public int ootdLikeCount(OotdLikeCountDTO ootdLikeCountDTO) {
        //2. 안눌렀을 경우, 좋아요 추가

        //좋아요를 누른 ootd글을 가져온다.
        Ootd ootd = entityManager.find(Ootd.class, ootdLikeCountDTO.getOotdIdx());
        //좋아요를 누른 사용자 정보를 가져온다.
        User user = userRepository.findUserByNickname(ootdLikeCountDTO.getNickname());
        if (ootdLikeCountDTO.getChk()) {//좋아요 +1
            ootd.setCount(ootd.getCount() + 1);
            //ootdlike테이블에 좋아요 한 정보 추가
            OotdLike ootdLikeFind = ootdLikeRepository.findOotdLikeByOotdIdxAndUser(ootdLikeCountDTO.getOotdIdx(), user);
            if (ootdLikeFind.equals(null)) {
                OotdLike ootdLike = new OotdLike();
                ootdLike.setFlag(true);
                ootdLike.setOotd(ootd);
                ootdLike.setUser(user);
                ootdLikeRepository.save(ootdLike);
            } else {
                ootdLikeFind.setFlag(true);
            }
        } else {// 좋아요 -1
            OotdLike ootdLike = ootdLikeRepository.findOotdLikeByOotdIdxAndUser(ootd.getIdx(), user);
            ootdLike.setFlag(false);
            ootd.setCount(ootd.getCount() - 1);
            ootdLikeRepository.save(ootdLike);
        }
        return ootd.getCount();
    }

    public List<OotdReplyDTO> deleteReply(OotdDeleteDTO ootdDeleteDTO) {
        OotdReply ootdReply = ootdReplyRepository.findReplyByIdx(ootdDeleteDTO.getReply_idx());
        ootdReply.setFlag(false);
        List<OotdReplyDTO> replyDTOList = new ArrayList<>();
        replyDTOList = replyList(ootdReply.getOotd().getIdx(), ootdReply.getUser().getNickname());
//        List<OotdReplyDTO> ootdReplyDTOList = new ArrayList<>();
//        List<OotdReply> ootdReplyList = ootdReplyRepository.findOotdReplyByOrderByWriteDate();
//        for (int i = 0; i < ootdReplyList.size(); i++) {
//            OotdReplyDTO ootdreplDTO = new OotdReplyDTO();
//            ootdreplDTO.setIdx(ootdReplyList.get(i).getIdx());
//            ootdreplDTO.setOotd_idx(ootdReplyList.get(i).getOotd().getIdx());
//            ootdreplDTO.setNickname(ootdReplyList.get(i).getUser().getNickname());
//            ootdreplDTO.setContent(ootdReplyList.get(i).getContent());
//            ootdreplDTO.setDepth(ootdReplyList.get(i).getDepth());
//            ootdreplDTO.setGroupNum(ootdReplyList.get(i).getGroupNum());
//            ootdreplDTO.setWrite_date(ootdReplyList.get(i).getWriteDate());
//            ootdreplDTO.setFlag(ootdReplyList.get(i).getFlag());
//            ootdReplyDTOList.add(ootdreplDTO);
//        }
        return replyDTOList;
    }

    public List<OotdReplyDTO> ootdReplyLike(Long idx, String nickname) {
        OotdReply reply = ootdReplyRepository.findOotdReplyByIdx(idx);
        User user = userRepository.findUserByNickname(nickname);

        OotdReplyLike replyLike = ootdReplyLikeRepository.findOotdReplyLikeByOotdReplyAndUser(reply, user);

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

        replyDTOList = replyList(reply.getOotd().getIdx(), reply.getUser().getNickname());

        return replyDTOList;

    }
    public List<OotdReplyDTO> replyList(Long idx, String nickname){
        List<OotdReplyDTO> replyDTOList = new ArrayList<>();

        Ootd ootd = ootdRepository.findOotdByIdx(idx);
        List<OotdReply> replyList = ootdReplyRepository.findOotdReplyByOotdOrderByWriteDate(ootd);
        User user = userRepository.findUserByNickname(nickname);

        for (OotdReply reply : replyList) {
            OotdReplyLike replyLike;
            replyLike = ootdReplyLikeRepository.findOotdReplyLikeByOotdReplyAndUser(reply, user);

            OotdReplyDTO replyDTO = new OotdReplyDTO();
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
            replyDTO.setFlag(reply.getFlag());

            replyDTOList.add(replyDTO);
        }

        return replyDTOList;
    }
}