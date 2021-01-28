package com.project.hunsu.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;

@Getter
@Setter
@Valid
@ToString
public class OotdUpdateDTO {
    Long ootdIdx;
    String hashtag;
    String content;
}
