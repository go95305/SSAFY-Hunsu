package com.project.hunsu.Service;

import com.project.hunsu.Dto.OotdDetail;
import com.project.hunsu.Dto.OotdMain;
import com.project.hunsu.Entity.Hashtag;
import com.project.hunsu.Entity.Ootd;
import com.project.hunsu.kakao.Repository.HashtagRepository;
import com.project.hunsu.kakao.Repository.OotdRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class OotdService {

    @PersistenceContext
    EntityManager entityManager;
    private final OotdRepository ootdRepository;
    private final HashtagRepository hashtagRepository;
    public OotdService(OotdRepository ootdRepository, HashtagRepository hashtagRepository) {
        this.ootdRepository = ootdRepository;
        this.hashtagRepository = hashtagRepository;
    }

    public List<OotdMain> SortByRecentOrPopularity(int sort) {
        List<OotdMain> ootdMainList = new ArrayList<>();
        List<Ootd> ootdList;
        List<Hashtag> hashtag = hashtagRepository.findAll();
        if (sort == 0)
            ootdList = ootdRepository.findAllByOrderByWriteDateAsc();
        else
            ootdList = ootdRepository.findAllByOrderByCountDesc();
        for (Ootd ootd : ootdList) {
            OotdMain ootdMain = new OotdMain();

//            OotdMain ootdMain = new OotdMain();
//            ootdMain.setOotdIdx(ootd.getIdx());
//            ootdMain.setContent(ootd.getContent());
//            ootdMain.setNickname(ootd.getUser().getNickname());
//            if (hashtag != null)
//                ootdMain.setHashtag(hashtag.getHashtag());
//            ootdMainList.add(ootdMain);
        }
        return ootdMainList;
    }


    public OotdDetail SpecificOotd(Long ootdIdx) {
        OotdDetail OotdDetail = ootdRepository.findByIdx(ootdIdx);
        return OotdDetail;
    }
}
