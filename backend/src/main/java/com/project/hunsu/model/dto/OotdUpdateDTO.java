package com.project.hunsu.model.dto;

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
