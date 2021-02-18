package com.project.hunsu.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

@Valid
@Getter
@Setter
public class MyPageDTO {
    private String myNickname;
    private String clickNickname;
}