package com.project.hunsu.Service;

import com.mysema.commons.lang.Assert;
import com.project.hunsu.Dto.OotdDetail;
import com.project.hunsu.Dto.OotdMain;
import com.project.hunsu.Entity.*;
import com.project.hunsu.kakao.Repository.HashtagRepository;
import com.project.hunsu.kakao.Repository.OotdLikeRepository;
import com.project.hunsu.kakao.Repository.OotdRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
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
        ootdRepository.deleteByIdx(idx);
    }

    public void likedown(Long ootdIdx, String nickName) {
        String query = "delete from OotdLike m where m.ootd.idx= :ootdIdx and m.user.nickname = :nickName";
        int result = entityManager.createQuery(query).setParameter("ootdIdx", ootdIdx).setParameter("nickName", nickName).executeUpdate();
    }
}
