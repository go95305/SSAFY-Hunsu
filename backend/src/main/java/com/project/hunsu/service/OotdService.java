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
        List<OotdMainDTO> ootdMainDTOList;
        QOotd ootd = QOotd.ootd;
        QHashtag hashtag = QHashtag.hashtag;
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        if (sort == 0) {//sort값 이 0이면 최신순으로 정렬
            ootdMainDTOList = jpaQueryFactory.select(Projections.fields(OotdMainDTO.class, ootd.idx.as("ootdIdx"), ootd.content.as("ootdContent"), hashtag.content.as("hashtagContent"), ootd.count.as("ootdLike")))
                    .from(ootd)
                    .leftJoin(hashtag).on(ootd.eq(hashtag.ootd))
                    .orderBy(ootd.writeDate.asc())
                    .fetch();
        } else {//sort값이 1이면 인기순으로 정렬
            ootdMainDTOList = jpaQueryFactory.select(Projections.fields(OotdMainDTO.class, ootd.idx.as("ootdIdx"), ootd.content.as("ootdContent"), hashtag.content.as("hashtagContent"), ootd.count.as("ootdLike")))
                    .from(ootd)
                    .leftJoin(hashtag).on(ootd.eq(hashtag.ootd))
                    .orderBy(ootd.count.desc())
                    .fetch();
        }
        return ootdMainDTOList;
    }


    //Ootd 상세페이지 정보 가져오기 //수정해
    public OotdDetailDTO SpecificOotd(Long ootdIdx) {
        Ootd Ootd = ootdRepository.findByIdx(ootdIdx);// OotdIdx를 통해 ootd글을 가져오기
        OotdDetailDTO ootdDetailDTO = new OotdDetailDTO();
        return ootdDetailDTO;
    }

    //Ootd글 삭제

    public void delete(Long idx) {
        //ootd와 관련된 연관관계 매핑 엔티티들과의 연결을 끊는다.(연관된 테이블의 레코드들을 전부 삭제) 그 이후에 Ootd테이블의 레코드 삭제
        List<Reply> reply = replyRepository.findReplyByOotdIdx(idx);
        for (Reply repl : reply
        ) {
            replyRepository.delete(repl);
        }

        List<Hashtag> hashtagList = hashtagRepository.findHashtagByOotdIdx(idx);
        for (Hashtag hs : hashtagList) {
            hashtagRepository.delete(hs);
        }


        List<OotdLike> ootdLikeList = ootdLikeRepository.findOotdLikeByOotdIdx(idx);
        for (OotdLike ol : ootdLikeList
        ) {
            ootdLikeRepository.delete(ol);
        }
        Ootd ot = entityManager.find(Ootd.class, idx);
        ootdRepository.delete(ot);
    }


    //좋아요 해제 시, 좋아요 테이블에서 좋아요를 누른 사람의 정보 삭제
    public void likedown(Long ootdIdx, String nickName) {
        String query = "delete from OotdLike m where m.ootd.idx= :ootdIdx and m.user.nickname = :nickName";
        entityManager.createQuery(query).setParameter("ootdIdx", ootdIdx).setParameter("nickName", nickName).executeUpdate();
    }

    //검색창에서 해시태그를 검색하거나 해시태그를 눌렀을때 해당해시태그가 포함된 모든 Ootd글들을 가져온다.
    public List<OotdMainDTO> searchByHashtag(String hashtag) {
        //우선 hashtag를 포함하는 모든 해시태그 테이블의 레코드를 가져온다.
        List<Hashtag> hashtagList = hashtagRepository.findByContentContaining(hashtag);
        List<OotdMainDTO> ootdMainDTOList = new ArrayList<>();

        for (Hashtag hs : hashtagList
        ) {
            // 각 레코드들의 idx를 통해 ootd레코드를 찾는다.
            OotdMainDTO ootdMainDTO = new OotdMainDTO();
            Ootd ootd = ootdRepository.findByIdx(hs.getOotd().getIdx());
            ootdMainDTO.setOotdIdx(ootd.getIdx());
            ootdMainDTO.setNickname(ootd.getUser().getNickname());
            ootdMainDTO.setOotdContent(ootd.getContent());
            ootdMainDTO.setHashtagContent(hs.getContent());
            ootdMainDTO.setOotdLike(ootd.getCount());
            ootdMainDTOList.add(ootdMainDTO);
        }
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
        ootd.setUpdated(false);
        ootdRepository.save(ootd);

        //만약 해시태그도 추가했다면 해시태그 테이블에도 레코드 추가해야한다.
        if (ootdWriteDTO.getHashtag() != null) {
            TypedQuery<Ootd> query = entityManager.createQuery("select o from Ootd o where o.user.nickname = :nickname and o.content = :content", Ootd.class);

            query.setParameter("nickname", ootdWriteDTO.getNickName()).setParameter("content", ootdWriteDTO.getContent());
            Ootd ot = query.getSingleResult();

            Hashtag hashtag = new Hashtag();
            hashtag.setContent(ootdWriteDTO.getHashtag());
            hashtag.setOotd(ot);
            hashtagRepository.save(hashtag);
        }


    }
}
