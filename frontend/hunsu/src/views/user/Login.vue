<template>
  <div style="margin: 20px">
    <h1>로그인화면</h1>
    <!-- <v-img
      width="400"
      src="@/assets/kakao_login_medium_wide.png"
      @click="loginWithKakao"
    >
    </v-img> -->
    <KakaoLogin
      api-key="ea2498225f7ae036eec6397d152e3497"
      image="kakao_login_btn_large"
      :on-success="onSuccess"
      :on-failure="onFailure"
    />
    <SignupInfo />
  </div>
</template>
 
<script>
import SignupInfo from "@/components/Mypage/SignupInfo";
import KakaoLogin from "vue-kakao-login";
import axios from "axios";

let onSuccess = (authObj) => {
  // 일단 로그인 요청
  // 백에서 액세스 토큰으로 유저정보 조회 후 일치하는 회원 없을 시 code '-1000' 리턴,
  // 일치하는 회원 있을 시 AccessToken과 RefreshToken 리턴
  console.log(authObj);
  let accessToken = authObj.access_token;
  console.log("accessTOken", accessToken);

  axios
    .post("http://localhost:8081/v1/auth/signin/kakao", accessToken)
    .then((result) => {
      console.log(result);
      if (result.code === "-1000") {
        // 일치하는 회원 없음 ( 회원가입 처리 )
      } else {
        // 일치하는 회원 있음 ( 로그인 처리 )
      }
    })
    .catch((e) => {
      console.log("error : ", e);
    });

  console.log("success");
};
let onFailure = (data) => {
  console.log(data);
  console.log("failure");
};

export default {
  name: "Login",
  components: {
    SignupInfo,
    KakaoLogin,
  },
  methods: {
    onSuccess,
    onFailure,
  },
};
</script>
 
<style>
</style>