package com.hindsight.authdemo.model.response;

import lombok.*;

import java.util.HashMap;

@Getter
@Setter
public class HashmapResult<T> extends CommonResult {
//    private List<T> list;
    private HashMap<T,T> map;
}

