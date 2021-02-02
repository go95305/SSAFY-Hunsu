<template>
  <div class="all" style="margin: 20px">
    <v-img src="@/assets/login_people.png" />
    <!-- <h1>로그인화면</h1> -->
    <div class="intro">
      <p>당신의 훈수를 보여줘</p>
    </div>

    <div class="loginButton">
      <KakaoLogin
        api-key="ea2498225f7ae036eec6397d152e3497"
        image="kakao_account_login_btn_medium_wide"
        :on-success="onSuccess"
        :on-failure="onFailure"
      />
    </div>
    <!-- <SignupInfo /> -->
  </div>
</template>
 
<script>
// import SignupInfo from "@/components/Mypage/SignupInfo";
import KakaoLogin from "vue-kakao-login";
import axios from "axios";

export default {
  name: "Login",
  components: {
    // SignupInfo,
    KakaoLogin,
  },
  methods: {
    onSuccess(authObj) {
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
    },
    onFailure(res) {
      console.log(res);
      console.log("failure");
    },
  },
};
</script>
 
<style>
@import url("https://fonts.googleapis.com/css2?family=East+Sea+Dokdo&display=swap");
.all {
  /* width: 100%;
  height: 100%; */
  top: 10%;
  position: relative;
}

.all .loginButton {
  position: absolute;
  /* left: 50%; */
  top: 130%;
  margin-left: 20px;
}

.intro {
  font-size: 50px;
  font-family: "East Sea Dokdo", cursive;
  text-align: center;
  top: 150%;
}
</style>