package com.project.hunsu.Service;

import com.project.hunsu.Dto.OotdDetail;
import com.project.hunsu.Dto.OotdMain;
import com.project.hunsu.Entity.*;
import com.project.hunsu.kakao.Repository.HashtagRepository;
import com.project.hunsu.kakao.Repository.OotdLikeRepository;
import com.project.hunsu.kakao.Repository.OotdRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class OotdService {

    @PersistenceContext
    EntityManager entityManager;
    private final OotdRepository ootdRepository;
    private final HashtagRepository hashtagRepository;
    private final OotdLikeRepository ootdLikeRepository;

    public OotdService(OotdRepository ootdRepository, HashtagRepository hashtagRepository, OotdLikeRepository ootdLikeRepository) {
        this.ootdRepository = ootdRepository;
        this.hashtagRepository = hashtagRepository;
        this.ootdLikeRepository = ootdLikeRepository;
    }

    public List<OotdMain> SortByRecentOrPopularity(int sort) {
        List<OotdMain> ootdMainList;
        QOotd ootd = QOotd.ootd;
        QHashtag hashtag = QHashtag.hashtag;
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        if (sort == 0) {
            ootdMainList = jpaQueryFactory.select(Projections.fields(OotdMain.class, hashtag.ootd.idx, hashtag.ootd.content.as("ootdContent"), hashtag.ootd.count, hashtag.content.as("hashtagContent")))
                    .from(hashtag)
                    .leftJoin(ootd).on(hashtag.ootd.eq(ootd))
                    .orderBy(ootd.writeDate.asc())
                    .fetch();
        } else {
            ootdMainList = jpaQueryFactory.select(Projections.fields(OotdMain.class, hashtag.ootd.idx, hashtag.ootd.content.as("ootdContent"), hashtag.ootd.count, hashtag.content.as("hashtagContent")))
                    .from(hashtag)
                    .leftJoin(ootd).on(hashtag.ootd.eq(ootd))
                    .orderBy(ootd.count.desc())
                    .fetch();
        }
////        List<OotdMain> ootdMainList = new ArrayList<>();
////        List<Ootd> ootdList;
////        List<Hashtag> hashtag = hashtagRepository.findAll();
//        if (sort == 0)
////            ootdList = ootdRepository.findAllByOrderByWriteDateAsc();
//        else
////            ootdList = ootdRepository.findAllByOrderByCountDesc();
//        for (Ootd ootd : ootdList) {
//            OotdMain ootdMain = new OotdMain();
//
////            OotdMain ootdMain = new OotdMain();
////            ootdMain.setOotdIdx(ootd.getIdx());
////            ootdMain.setContent(ootd.getContent());
////            ootdMain.setNickname(ootd.getUser().getNickname());
////            if (hashtag != null)
////                ootdMain.setHashtag(hashtag.getHashtag());
////            ootdMainList.add(ootdMain);

//        }
        return ootdMainList;
    }


    public OotdDetail SpecificOotd(Long ootdIdx) {
        Ootd Ootd = ootdRepository.findByIdx(ootdIdx);
        OotdDetail ootdDetail = new OotdDetail();
        return ootdDetail;
    }

    public void delete(Long idx) {
        ootdRepository.deleteByIdx(idx);
    }

    public void likeDown(Long ootdIdx, String nickname) {
        ootdLikeRepository.deleteByIdxAndNickname(ootdIdx,nickname);

    }
}
