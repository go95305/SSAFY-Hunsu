package com.project.hunsu.Service;

import com.project.hunsu.Dto.OotdDetailDTO;
import com.project.hunsu.Dto.OotdMainDTO;
import com.project.hunsu.Dto.OotdWriteDTO;
import com.project.hunsu.Entity.*;
import com.project.hunsu.kakao.Repository.HashtagRepository;
import com.project.hunsu.kakao.Repository.OotdLikeRepository;
import com.project.hunsu.kakao.Repository.OotdRepository;
import com.project.hunsu.kakao.Repository.ReplyRepository;
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


    public List<OotdMainDTO> SortByRecentOrPopularity(int sort) {
        List<OotdMainDTO> ootdMainDTOList;
        QOotd ootd = QOotd.ootd;
        QHashtag hashtag = QHashtag.hashtag;
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        if (sort == 0) {
            ootdMainDTOList = jpaQueryFactory.select(Projections.fields(OotdMainDTO.class, ootd.idx.as("ootdIdx"), ootd.content.as("ootdContent"), hashtag.content.as("hashtagContent"), ootd.count.as("ootdLike")))
                    .from(ootd)
                    .leftJoin(hashtag).on(ootd.eq(hashtag.ootd))
                    .orderBy(ootd.writeDate.asc())
                    .fetch();
        } else {
            ootdMainDTOList = jpaQueryFactory.select(Projections.fields(OotdMainDTO.class, ootd.idx.as("ootdIdx"), ootd.content.as("ootdContent"), hashtag.content.as("hashtagContent"), ootd.count.as("ootdLike")))
                    .from(ootd)
                    .leftJoin(hashtag).on(ootd.eq(hashtag.ootd))
                    .orderBy(ootd.count.desc())
                    .fetch();
        }
        return ootdMainDTOList;
    }


    public OotdDetailDTO SpecificOotd(Long ootdIdx) {
        Ootd Ootd = ootdRepository.findByIdx(ootdIdx);
        OotdDetailDTO ootdDetailDTO = new OotdDetailDTO();
        return ootdDetailDTO;
    }

    public void delete(Long idx) {
        //ootd와 관련된 연관관계 매핑 엔티티들과의 연결을 끊는다.

        List<Reply> reply = replyRepository.findReplyByOotdIdx(idx);
        for (Reply repl : reply
        ) {
//            replyRepository.delete(repl);
            repl.setOotd(null);
        }

        List<Hashtag> hashtagList = hashtagRepository.findHashtagByOotdIdx(idx);
        for (Hashtag hs : hashtagList) {
//            hashtagRepository.delete(hs);
            hs.setOotd(null);
        }


        List<OotdLike> ootdLikeList = ootdLikeRepository.findOotdLikeByOotdIdx(idx);
        for (OotdLike ol : ootdLikeList
        ) {
//            ootdLikeRepository.delete(ol);
            ol.setOotd(null);
        }
        Ootd ot = entityManager.find(Ootd.class, idx);
        ootdRepository.delete(ot);
    }


    public void likedown(Long ootdIdx, String nickName) {
        String query = "delete from OotdLike m where m.ootd.idx= :ootdIdx and m.user.nickname = :nickName";
        int result = entityManager.createQuery(query).setParameter("ootdIdx", ootdIdx).setParameter("nickName", nickName).executeUpdate();
    }

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

    public void write(OotdWriteDTO ootdWriteDTO) {
        Ootd ootd = new Ootd();
        User user = entityManager.find(User.class, ootdWriteDTO.getNickName());

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


            System.out.println(ot.getUser().getNickname());

            Hashtag hashtag = new Hashtag();
            hashtag.setContent(ootdWriteDTO.getHashtag());
            hashtag.setOotd(ot);
            hashtagRepository.save(hashtag);
        }


    }
}
