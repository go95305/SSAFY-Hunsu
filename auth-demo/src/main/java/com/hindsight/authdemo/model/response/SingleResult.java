package com.hindsight.authdemo.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult<T> extends CommonResult {// <T> 붙이면 어떤 형태도 가
    private T data;
}
