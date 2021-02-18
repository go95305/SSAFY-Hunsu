<template>
  <!-- 정렬 버튼 -->
  <v-btn-toggle id="ootd_filter" tile color="red accent-3">
    <v-btn small @click="recentCall"> 최신순 </v-btn>
    <v-btn small @click="popularCall"> 인기순 </v-btn>
  </v-btn-toggle>
</template>

<script>
// import axios from "
import { EventBus } from "@/services/eventBus";

export default {
  name: "OotdFilter",
  methods: {
    recentCall() {
      EventBus.$emit("recent");
    },
    popularCall() {
      EventBus.$emit("popular");
    },
  },
};
</script>

<style>
#ootd_filter {
  height: 28px;
  margin-left: 5%;
  margin-top: 3%;
}
</style>