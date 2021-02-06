<template>
  <div style="text-align: center; margin-top: 10%">
    <br /><br />
    <v-img src="@/assets/hunsulogo.png" width="200" id="logo" /><br /><br />
    <b>👏 방문해주셔서 감사해요! 👏</b><br /><br />
    👌 현재 완료된 기능 👌<br />
    1. #OOTD 작성, 수정, 삭제, 좋아요 <br />
    2. 뭘입을까 작성, 수정, 삭제, 좋아요, 댓글 <br />
    <br />
    🙏 미완료 기능 🙏<br />
    1. 유저관련 기능(조회 및 마이페이지 등) <br />
    2. 실채훈 <br />
    3. 추천 서비스 <br />
    <br />
    모든 질문은 게시판을 이용해주시거나<br />
    gkswogml23@naver.com<br />
    한재희 매터모스트 DM 주세요!! 감사합니다 👍 <br /><br /><br />
    <v-btn large style="primary" @click="goToHome()">훈수 입장하기!</v-btn>
  </div>
</template>
<script>
export default {
  name: "Description",
  mounted() {
    console.log("mo");
  },
  methods: {
    goToHome() {
      this.$router.push("/Home");
    },
  },
};
</script>
<style>
</style>