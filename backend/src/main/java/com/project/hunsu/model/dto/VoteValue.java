package com.project.hunsu.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VoteValue {
    private Long idx;
    private int count;
    private Boolean choice;
}
