package com.project.hunsu.Dto;

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
