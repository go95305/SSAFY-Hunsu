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
  // data() {
  //   return {
  //     ootdList: [],
  //   };
  // },
  methods: {
    ...mapActions(["getOotdListInApi"]),
    // getRecentOotdList() {
    //   axios.get("http://i4c102.p.ssafy.io:8080/api/ootd/0")
    //     .then((res) => {
    //       this.ootdList = []
    //       this.ootdList.push(res.data)
    //       console.log(this.ootdList)
    //     })
    //     .catch((err) => {
    //       console.error(err);
    //     });
    // },
    // getPopularOotdList() {
    //   axios.get("http://i4c102.p.ssafy.io:8080/api/ootd/1")
    //     .then((res) => {
    //       this.ootdList = []
    //       this.ootdList.push(res.data)
    //       console.log(this.ootdList)
    //     })
    //     .catch((err) => {
    //       console.error(err);
    //     });
    // }
  },
};
</script>

<style>
</style>