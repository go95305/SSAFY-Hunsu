<template>
<!-- 라이브 메인페이지 -->
<div>
  <v-card @click="goToLiveDetail">
    <!-- 라이브 메인 사진 -->
    <v-img
      src="https://picsum.photos/350/165?random"
      max-height="125"
      contain
      class="grey darken-4"
    ></v-img>
    <!-- 라이브 제목 -->
    <v-card-title class="title text-body-1">
      한시간 뒤에 소개팅가요 ㅠㅠ 도와주세요
    </v-card-title>
    <!-- 작성자, 접속자수 -->
      <div class="d-flex align-center justify-center">
        <v-list-item-avatar>
          <v-img src="https://cdn.vuetifyjs.com/images/john.png"></v-img>
        </v-list-item-avatar>
        <v-card-title class="text-body-1" id="writer">
          작성자
        </v-card-title>
      </div>
  </v-card>
</div>

</template>

<script>
export default {
  name: "LiveList",
  data() {
    return {
      dialog: false,
      notifications: false,
      sound: true,
      widgets: false,
    }
  },
  methods: {
    goToLiveDetail() {
      this.$router.push('/live/detail')
    }
  }
}
</script>

<style scoped>

</style>