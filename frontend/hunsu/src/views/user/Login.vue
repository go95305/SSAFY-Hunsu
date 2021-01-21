<template>
  <div id="app">
    <KakaoLogin
      api-key="f5da323ce45a9a6242961432e331ee70"
      image="kakao_login_btn_large"
      :on-success=onSuccess
      :on-failure=onFailure
      />
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
  name: 'App',
  components: {
    KakaoLogin
  },
  methods: {
    onSuccess,
    onFailure
  }
}
</script>
 
<style>
</style>