package com.hindsight.authdemo.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MapResult<T> extends CommonResult {
    private Map<T, T> map;
}
