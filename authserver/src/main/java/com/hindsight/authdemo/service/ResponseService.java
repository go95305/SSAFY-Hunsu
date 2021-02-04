package com.hindsight.authdemo.service;

import com.hindsight.authdemo.model.response.CommonResult;
import com.hindsight.authdemo.model.response.TokenResult;
import com.hindsight.authdemo.model.response.SingleResult;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service // 해당 Class가 Service임을 명시합니다.
public class ResponseService {

    // enum으로 api 요청 결과에 대한 code, message를 정의합니다.
    public enum CommonResponse {
        SUCCESS(0, "성공하였습니다."),
        FAIL(-1, "실패하였습니다.");

        int code;
        String msg;

        CommonResponse(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
    // 단일건 결과를 처리하는 메소드
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }
    // 다중건 결과를 처리하는 메소드
//    public Map<T> MapResult<T> getMapResult(Map<T, T> map) {
//        MapResult<T> result = new MapResult<>();
//        result.setMap(map);
//        setSuccessResult(result);
//        return result;
//    }

    // 다중건 결과를 처리하는 메소드
//    public <T> TokenResult<T> getHashmapResult(Map<T,T> map) {
//        TokenResult<T> result = new TokenResult<>();
//        result.setMap(map);
//        setSuccessResult(result);
//        return result;
//    }
    // 성공 결과만 처리하는 메소드
    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }
    // 실패 결과만 처리하는 메소드
    public CommonResult getFailResult(int code, String msg) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
}
