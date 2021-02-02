<template>
  <!-- WHATWEAR 메인 페이지 -->
  <div>
    <!--v-for 사용을 위한 최상위 div-->
    <v-card
      v-for="(whatwear, idx) in whatwearList"
      :key="idx"
      class="mx-auto mt-10"
      max-width="600px"
      height="65px"
      @click="goToWhatWearDetail(whatwear)"
    >
      <div class="d-flex">
        <!--프로필사진-->
        <v-avatar class="mt-2 ml-4">
          <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" />
        </v-avatar>
        <!--유저닉네임-->
        <v-card-subtitle class="mt-1">
          {{ whatwear.nickname }}
        </v-card-subtitle>
        <!--뭘입을까 글제목-->
        <v-card-subtitle class="mt-1 font-weight-bold">
          {{ whatwear.title }}
        </v-card-subtitle>
        <!--투표기능뱃지-->
        <v-badge
          v-if="whatwear.voteActivated"
          color="red accent-3"
          content="투표"
          inline
          class="mt-6"
        >
        </v-badge>
      </div>
    </v-card>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "WhatWearList",
  data() {
    return {
      whatwearList: [],
    };
  },
  created() {
    this.getWhatWearList();
  },
  methods: {
    goToWhatWearDetail(whatwear) {
      this.$router.push(`/whatwear/${whatwear.wear_idx}/${whatwear.nickname}`);
      // this.$router.push({name: 'Whatweardetail', params:{
      //   nickName : whatwear.nickname,
      //   wear_idx : whater~~
      // }})
    },
    getWhatWearList() {
      axios
        .get("http://i4c102.p.ssafy.io:8080/api/wear")
        .then((res) => {
          this.whatwearList = res.data;
        })
        .catch((err) => {
          console.error(err);
        });
    },
  },
};
</script>

<style>
</style>