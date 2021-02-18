package com.project.hunsu.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class WearMainTotalDTO {
    private Long count;
    private List<WearMainDTO> wearMainDTOList = new ArrayList<>();
}
