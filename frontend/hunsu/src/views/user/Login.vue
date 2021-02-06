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
// import axios from "axios";
import { mapGetters, mapActions } from "vuex";

export default {
  name: "Login",
  computed: {
    ...mapGetters(["getAccessToken, getRefreshToken"]),
  },
  components: {
    // SignupInfo,
    KakaoLogin,
  },
  methods: {
    ...mapActions(["userCheck", "kakaoLogin"]),
    onSuccess(authObj) {
      console.log("auth", authObj);
      let router = this.$router; // 임시방편

      this.userCheck({
        accessToken: authObj.access_token,
        refreshToken: authObj.refresh_token,
      })
        .then((res) => {
          console.log("return userchk 2", res);
          if (res === -1) {
            router.push({
              name: "SignUp",
              params: {
                accessToken: authObj.access_token,
                refreshToken: authObj.refresh_token,
              },
            });
          } else {
            this.kakaoLogin();
          }
        })
        .catch((err) => {
          console.log("error in userCheck ", err);
        });
      // console.log("in login", result);
      // 가입여부체크
      // if (this.userCheck(authObj.access_token, authObj.refresh_token) == -1) {
      //   // 회원가입
      //   console.log("회원가입 넘어가즈아");
      //   this.$router.push({
      //     name: "SignUp",
      //     params: {
      //       accessToken: authObj.access_token,
      //       refreshToken: authObj.refresh_token,
      //     },
      //   });
      // }

      // console.log("success");
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