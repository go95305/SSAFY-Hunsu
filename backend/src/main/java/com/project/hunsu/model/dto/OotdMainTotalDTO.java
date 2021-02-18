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
public class OotdMainTotalDTO {
    private Long count;
    private List<OotdMainDTO> OotdMainDTOList = new ArrayList<>(); //hashtag에서 가져오기
}