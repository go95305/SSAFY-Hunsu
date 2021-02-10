package com.project.hunsu.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

@Valid
@Getter
@Setter
public class ProfileDTO {
    private String nickname;
    private Double height;
    private String size;
}