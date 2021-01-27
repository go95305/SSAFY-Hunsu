<template>
  <div style="margin: 20px">
    <h1>로그인화면</h1>
    <v-img
      width="400"
      src="@/assets/kakao_login_medium_wide.png"
      @click="loginWithKakao">
    </v-img>
    <SignupInfo />
  </div>
</template>
 
<script>
import SignupInfo from "@/components/Mypage/SignupInfo"
export default {
  name: "Login",
  components: {
    SignupInfo
  },
  methods: {
    loginWithKakao() {
      const params = {
        redirectUri: "http://localhost:8080/auth"
      }
      window.Kakao.Auth.authorize(params)
    }
  }
}
</script>
 
<style>
</style>