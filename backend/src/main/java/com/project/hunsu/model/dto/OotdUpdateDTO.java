package com.project.hunsu.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Valid
@ToString
public class OotdUpdateDTO {
    Long ootdIdx;
    List<String> hashtagList = new ArrayList<>();
    String content;
}