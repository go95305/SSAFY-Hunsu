package com.project.hunsu.Controller;

import com.project.hunsu.dto.Wear;
import com.project.hunsu.kakao.Repository.WearRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WearController {

    private final WearRepository wearRepository;

    public WearController(WearRepository wearRepository) {
        this.wearRepository = wearRepository;
    }

    @GetMapping("/wear")
    public List<Wear> wearMain(@RequestParam int sort) {
        List<Wear> wearList = new ArrayList<>();
        if (sort == 0) {
            //최신순
            wearList = wearRepository.findWearByOrderByWriteDate();
        } else {
            //인기순
            wearList = wearRepository.findWearByOrderByCountDesc();
        }
        return wearList;
    }

    @GetMapping("/wear/detail")
    public Wear detailOotd(@RequestParam Long idx) {
        Wear wearDetail = wearRepository.findWearByIdx(idx);
        return wearDetail;
    }

}
