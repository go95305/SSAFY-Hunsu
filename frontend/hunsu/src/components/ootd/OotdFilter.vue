<template>
  <!-- 정렬 버튼 -->
  <v-btn-toggle id="ootd_filter" tile color="red accent-3">
    <v-btn small @click="recentCall"> 최신순 </v-btn>
    <v-btn small @click="popularCall"> 인기순 </v-btn>
  </v-btn-toggle>
</template>

<script>
// import axios from "axios";
// import { mapGetters } from "vuex";
import { EventBus } from "@/services/eventBus";

export default {
  name: "OotdFilter",
  computed: {
    // ...mapGetters(["getOotdList"]),
    // pageNumCount() {
    //   if (this.getOotdList) {
    //     return parseInt(this.getOotdList.length / 6) + 1;
    //   }
    //   return "";
    // },
  },
  methods: {
    recentCall() {
      EventBus.$emit("recent");
    },
    popularCall() {
      EventBus.$emit("popular");
    },
  },
  // ...mapActions([
  //   "getOotdListInApi",
  //   "getOotdInfoInApi",
  //   "getImageList",
  //   "getProfileInfoInApi",
  //   "getProfileImage",
  //   "getProfiles",
  // ]),
  //   async getRecentOotdListInApi() {
  //     let root = this;
  //     await this.getOotdListInApi({
  //       sort: 0,
  //       pageNum: this.pageNumCount,
  //     });
  //     root.getProfiles(this.getOotdList);
  //     this.getOotdList.forEach((info) => {
  //       root.getImageList({ prefix: "ootd/" + info.ootdIdx }).then((res) => {
  //         info.imageUrls = res;
  //       });
  //     });
  //   },
  //   async getPopularOotdListInApi() {
  //     let root = this;
  //     await this.getOotdListInApi({
  //       sort: 1,
  //       pageNum: this.pageNumCount,
  //     });
  //     root.getProfiles(this.getOotdList);
  //     this.getOotdList.forEach((info) => {
  //       root.getImageList({ prefix: "ootd/" + info.ootdIdx }).then((res) => {
  //         info.imageUrls = res;
  //       });
  //     });
  //   },
  // },
};
</script>

<style>
#ootd_filter {
  height: 28px;
  margin-left: 5%;
  margin-top: 3%;
}
</style>