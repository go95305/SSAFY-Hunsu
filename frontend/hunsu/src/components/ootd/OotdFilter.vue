<template>
  <!-- 정렬 버튼 -->
  <v-btn-toggle tile color="red accent-3" group>
    <v-btn value="left" @click="getOotdListInApi(0)"> 최신순 </v-btn>

    <v-btn value="center" @click="getOotdListInApi(1)"> 인기순 </v-btn>
  </v-btn-toggle>
</template>

<script>
// import axios from "axios";
import { mapActions } from "vuex";
export default {
  name: "OotdFilter",

  methods: {
    ...mapActions(["getOotdListInApi"]),
  },
};
</script>

<style>
</style>