<template>
  <!-- 정렬 버튼 -->
  <v-btn-toggle tile color="red accent-3" group>
    <v-btn @click="getRecentOotdListInApi()"> 최신순 </v-btn>

    <v-btn @click="getPopularOotdListInApi()"> 인기순 </v-btn>
  </v-btn-toggle>
</template>

<script>
// import axios from "axios";
import { mapActions, mapGetters } from "vuex";
export default {
  name: "OotdFilter",
  computed: {
    ...mapGetters(["getOotdList"]),
    pageNumCount() {
      if (this.getOotdList) {
        return parseInt(this.getOotdList.length / 6) + 1;
      }
      return "";
    },
  },
  methods: {
    ...mapActions([
      "getOotdListInApi",
      "getOotdInfoInApi",
      "getImageList",
      "getProfileInfoInApi",
      "getProfileImage",
      "getProfiles",
    ]),
    async getRecentOotdListInApi() {
      let root = this;
      await this.getOotdListInApi({
        sort: 0,
        pageNum: this.pageNumCount,
      });
      root.getProfiles(this.getOotdList);
      this.getOotdList.forEach((info) => {
        root.getImageList({ prefix: "ootd/" + info.ootdIdx }).then((res) => {
          info.imageUrls = res;
        });
      });
    },
    async getPopularOotdListInApi() {
      let root = this;
      await this.getOotdListInApi({
        sort: 1,
        pageNum: this.pageNumCount,
      });
      root.getProfiles(this.getOotdList);
      this.getOotdList.forEach((info) => {
        root.getImageList({ prefix: "ootd/" + info.ootdIdx }).then((res) => {
          info.imageUrls = res;
        });
      });
    },
  },
};
</script>

<style>
</style>