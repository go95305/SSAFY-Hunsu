package com.project.hunsu.Controller;

import com.project.hunsu.dto.Ootd;
import com.project.hunsu.kakao.Repository.KakaoRepository;
import com.project.hunsu.kakao.Repository.OotdRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OotdController {

    private final OotdRepository ootdRepository;

    public OotdController(OotdRepository ootdRepository) {
        this.ootdRepository = ootdRepository;
    }

    @GetMapping("/ootd")
    public List<Ootd> ootdMain(@RequestParam int sort) {
        List<Ootd> ootdList = new ArrayList<>();
        if (sort == 0) {
            //최신순
            ootdList = ootdRepository.findOotdByOrderByWriteDate();
        } else {
            //인기순
            ootdList = ootdRepository.findOotdByOrderByCountDesc();
        }
        for (int i = 0; i < ootdList.size(); i++) {
            System.out.println(ootdList.get(i).getIdx());
        }
        return ootdList;
    }

    @GetMapping("/ootd/detail")
    public Ootd detailOotd(@RequestParam Long idx) {
        Ootd ootdDetail = ootdRepository.findOotdByIdx(idx);
        System.out.println();
        return ootdDetail;
    }

}
