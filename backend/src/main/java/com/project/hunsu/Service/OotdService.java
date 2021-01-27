package com.project.hunsu.Service;

import com.project.hunsu.Dto.OotdDetail;
import com.project.hunsu.Dto.OotdMain;
import com.project.hunsu.Entity.*;
import com.project.hunsu.kakao.Repository.HashtagRepository;
import com.project.hunsu.kakao.Repository.OotdLikeRepository;
import com.project.hunsu.kakao.Repository.OotdRepository;
import com.project.hunsu.kakao.Repository.ReplyRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.project.hunsu.Entity.QReply.reply;

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


    public List<OotdMain> SortByRecentOrPopularity(int sort) {
        List<OotdMain> ootdMainList;
        QOotd ootd = QOotd.ootd;
        QHashtag hashtag = QHashtag.hashtag;
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        if (sort == 0) {
            ootdMainList = jpaQueryFactory.select(Projections.fields(OotdMain.class, ootd.idx.as("ootdIdx"), ootd.content.as("ootdContent"), hashtag.content.as("hashtagContent"), ootd.count.as("ootdLike")))
                    .from(ootd)
                    .leftJoin(hashtag).on(ootd.eq(hashtag.ootd))
                    .orderBy(ootd.writeDate.asc())
                    .fetch();
        } else {
            ootdMainList = jpaQueryFactory.select(Projections.fields(OotdMain.class, ootd.idx.as("ootdIdx"), ootd.content.as("ootdContent"), hashtag.content.as("hashtagContent"), ootd.count.as("ootdLike")))
                    .from(ootd)
                    .leftJoin(hashtag).on(ootd.eq(hashtag.ootd))
                    .orderBy(ootd.count.desc())
                    .fetch();
        }
        return ootdMainList;
    }


    public OotdDetail SpecificOotd(Long ootdIdx) {
        Ootd Ootd = ootdRepository.findByIdx(ootdIdx);
        OotdDetail ootdDetail = new OotdDetail();
        return ootdDetail;
    }

    public void delete(Long idx) {
        //ootd와 관련된 연관관계 매핑 엔티티들과의 연결을 끊는다.

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


    public void likedown(Long ootdIdx, String nickName) {
        String query = "delete from OotdLike m where m.ootd.idx= :ootdIdx and m.user.nickname = :nickName";
        int result = entityManager.createQuery(query).setParameter("ootdIdx", ootdIdx).setParameter("nickName", nickName).executeUpdate();
    }
}
