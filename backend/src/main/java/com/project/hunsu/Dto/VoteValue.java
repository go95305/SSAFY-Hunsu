package com.project.hunsu.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class VoteValue {
    private Long idx;
    private int count;
    private Boolean choice;
}
