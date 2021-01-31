package com.project.hunsu.service;

import com.project.hunsu.model.dto.OotdDetailDTO;
import com.project.hunsu.model.dto.OotdMainDTO;
import com.project.hunsu.model.dto.OotdWriteDTO;
import com.project.hunsu.repository.HashtagRepository;
import com.project.hunsu.repository.OotdLikeRepository;
import com.project.hunsu.repository.OotdRepository;
import com.project.hunsu.repository.ReplyRepository;
import com.project.hunsu.model.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    private final ReplyRepository replyRepository;

    public OotdService(OotdRepository ootdRepository, HashtagRepository hashtagRepository, OotdLikeRepository ootdLikeRepository, ReplyRepository replyRepository) {
        this.ootdRepository = ootdRepository;
        this.hashtagRepository = hashtagRepository;
        this.ootdLikeRepository = ootdLikeRepository;
        this.replyRepository = replyRepository;
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
            if (ootdList.get(i).getIsActivated()) {
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
        List<Reply> replyList = replyRepository.findReplyByOotdIdx(ootdIdx);
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
        ootdDetailDTO.setReplyList(replyList);
        return ootdDetailDTO;
    }

    //Ootd글 삭제
    public void delete(Long idx) {
        //ootd와 관련된 연관관계 매핑 엔티티들과의 연결을 끊는다.(연관된 테이블의 레코드들을 전부 삭제) 그 이후에 Ootd테이블의 레코드 삭제
        List<Reply> reply = replyRepository.findReplyByOotdIdx(idx);
        for (Reply repl : reply
        ) {
            repl.setIsActivated(false); //해당 ootd글과 연관된 모든 댓글들은 전부 비활성화
        }

        Ootd ot = entityManager.find(Ootd.class, idx);
        ot.setIsActivated(false); // 해당 ootd 글 비활성화
    }


    //좋아요 해제 시, 좋아요 테이블에서 좋아요를 누른 사람의 정보 삭제
    public void likedown(Long ootdIdx, String nickName) {
        String query = "delete from OotdLike m where m.ootd.idx= :ootdIdx and m.user.nickname = :nickName";
        entityManager.createQuery(query).setParameter("ootdIdx", ootdIdx).setParameter("nickName", nickName).executeUpdate();
    }

    //검색창에서 해시태그를 검색하거나 해시태그를 눌렀을때 해당해시태그가 포함된 모든 Ootd글들을 가져온다.
    public List<OotdMainDTO> searchByHashtag(String hashtag) {
        //우선 hashtag를 포함하는 모든 해시태그 테이블의 레코드를 가져온다.
        List<Hashtag> hashtagList = hashtagRepository.findByContent(hashtag); // 1. 우선 해시태그가 포함된 모든 hashtag 레코드 가져오기
        List<OotdMainDTO> ootdMainDTOList = new ArrayList<>();
        for (int i = 0; i < hashtagList.size(); i++) {
            OotdMainDTO ootdMainDTO = new OotdMainDTO();
            Ootd ootd = ootdRepository.findByIdx(hashtagList.get(i).getOotd().getIdx());
            if (ootd.getIsActivated()) {
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
    public void write(OotdWriteDTO ootdWriteDTO) {
        Ootd ootd = new Ootd();
        User user = entityManager.find(User.class, ootdWriteDTO.getNickName());//ootd테이블의 닉네임에 맞는 유저정보를 가져온다.

        //ootd글 새롭게 생성
        ootd.setUser(user);
        ootd.setContent(ootdWriteDTO.getContent());
        ootd.setCount(0);
        ootd.setIsUpdated(false);
        ootdRepository.save(ootd);

        //만약 해시태그도 추가했다면 해시태그 테이블에도 레코드 추가해야한다.
        if (ootdWriteDTO.getHashtagList().size() != 0) {
            TypedQuery<Ootd> query = entityManager.createQuery("select o from Ootd o where o.user.nickname = :nickname and o.content = :content", Ootd.class);

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
}
