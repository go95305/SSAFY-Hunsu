package com.project.hunsu.model.dto;

import com.project.hunsu.model.entity.Hashtag;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OotdWriteDTO {
    private String nickName;
    private String content;
    private List<String> hashtagList=new ArrayList<>();
}
