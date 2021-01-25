<template>
  <div>
    <h1>로그인화면</h1>
    <v-img
      width="400"
      src="@/assets/kakao_login_medium_wide.png"
      @click="loginWithKakao">
    </v-img>
  </div>
</template>
 
<script>
import KakaoLogin from 'vue-kakao-login'
 
let onSuccess = (data) => {
  console.log(data)
  console.log("success")
}
let onFailure = (data) => {
  console.log(data)
  console.log("failure")
}
 
export default {
  name: "Login",
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