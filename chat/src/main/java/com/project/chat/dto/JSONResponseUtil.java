package com.project.chat.dto;

import net.sf.json.JSONSerializer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class JSONResponseUtil{

    /**
     * JSON View 화면 처리를 위해 JSON변환 후 ResponseEntity로 반환.
     * @param obj
     * @return
     */
    public static ResponseEntity<String> getJSONResponse(Object obj){
        String json = JSONSerializer.toJSON(obj).toString();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        return new ResponseEntity<>(json, responseHeaders, HttpStatus.OK);
    }

    /**
     * JSON View 화면 처리를 위해 JSON변환 후 ResponseEntity로 반환.
     * @param obj
     * @return
     */
    public static String getJSONString(Object obj){
        return JSONSerializer.toJSON(obj).toString();
    }
}


