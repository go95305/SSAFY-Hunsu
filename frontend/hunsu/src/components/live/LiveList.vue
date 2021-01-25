<template>
<div>
  <v-card @click="goToLiveDetail">
    <v-img
      src="https://picsum.photos/350/165?random"
      max-height="125"
      contain
      class="grey darken-4"
    ></v-img>
    <v-card-title class="title text-body-1">
      한시간 뒤에 소개팅가요 ㅠㅠ 도와주세요
    </v-card-title>
    <v-card-title class="title text-body-1">
      작성자 접속자수
    </v-card-title>
  </v-card>
</div>
</template>

<script>
export default {
  name: "LiveList",
  methods: {
    goToLiveDetail() {
      this.$router.push('/live/detail')
    }
  }
}
</script>

<style>

</style>