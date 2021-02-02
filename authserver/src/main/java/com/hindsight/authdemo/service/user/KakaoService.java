package com.hindsight.authdemo.service.user;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hindsight.authdemo.advice.exception.CComunicationException;
import com.hindsight.authdemo.model.social.KakaoProfile;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class KakaoService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;
    private final Environment env;
    private final Gson gson;

    @Value("${spring.url.base}")
    private String baseUrl;
    @Value("${spring.social.kakao.client_id}")
    private String kakaoClientId;
    @Value("${spring.social.kakao.redirect}")
    private String kakaoRedirect;
    @Value("${spring.social.kakao.url.token}")
    private String kakaoTokenUrl;
    @Value("${spring.social.kakao.client_secret}")
    private String kakaoSecret;
    @Value("${spring.social.kakao.url.profile}")
    private String kakaoProfileUrl;

    public KakaoProfile getKakaoProfile(String accessToken){
        //header : Content-type: application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        //set http entity
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        try{
            // Request profile
            ResponseEntity<String> response = restTemplate.postForEntity(kakaoProfileUrl, request, String.class);
            if(response.getStatusCode() == HttpStatus.OK)
                return gson.fromJson(response.getBody(), KakaoProfile.class);
        }catch(Exception e){
            throw new CComunicationException();
        }
        throw new CComunicationException();
    }

    public String reKakaoAccessToken(String refreshToken){

        // HttpHeader 오브젝트 생성
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers =new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        // HttpBody 오브젝트 생성
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
        param.add("grant_type","refresh_token");
        param.add("client_id","8e53809b2827c367b5aa27ac70dfd124");
        param.add("refresh_token",refreshToken);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest= new HttpEntity<>(param,headers);

        // Http 요청하기 - Post방식으로
        ResponseEntity<String> response = rt.exchange(
                "kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(String.valueOf(response));

        String accessToken = element.getAsJsonObject().get("access_token").getAsString();

        return  accessToken;
    }


    //코드받는거???

}
