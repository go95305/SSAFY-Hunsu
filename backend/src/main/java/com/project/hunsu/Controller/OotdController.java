package com.project.hunsu.Controller;

import com.project.hunsu.kakao.Repository.OotdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OotdController {

    @Autowired
    OotdRepository ootdRepository;

    @GetMapping("/ootd")
    public Object OotdList(@RequestBody)



}
