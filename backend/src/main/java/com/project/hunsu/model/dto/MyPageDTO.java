package com.project.hunsu.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

@Valid
@Getter
@Setter
public class UserFollowDTO {
    private String myNickname;
    private String yourNickname;
}