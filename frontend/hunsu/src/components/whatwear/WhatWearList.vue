<template>
  <!-- WHATWEAR 메인 페이지 -->

  <!--v-for 사용을 위한 최상위 div-->
  <v-card flat>
    <v-card
      v-for="(whatwear, idx) in whatwearList"
      :key="idx"
      @click="goToWhatwearDetail(whatwear)"
      flat
    >
      <div class="d-flex">
        <!--프로필사진-->
        <!-- <div class="mt-2"></div> -->
        <v-avatar class="mt-5 ml-4">
          <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" />
        </v-avatar>
        <!--유저닉네임-->
        <v-card-subtitle class="mt-4 font-weight-bold">
          {{ whatwear.nickname }}
        </v-card-subtitle>
        <!--뭘입을까 글제목-->
        <v-card-subtitle class="mt-4 font-weight-bold">
          {{ whatwear.title }}
        </v-card-subtitle>
        <!--투표기능뱃지-->
        <v-badge
          v-if="whatwear.voteActivated"
          color="red accent-3"
          content="투표"
          inline
          class="mt-9"
        >
        </v-badge>
        <!-- <div class="mb-2"></div> -->
      </div>
    </v-card>
  <!-- <div class="text-center">
    <v-container>
      <v-row justify="center">
        <v-col cols="8">
          <v-container class="max-width">
            <v-pagination
              v-model="page"
              class="my-4"
              :length="2"
            ></v-pagination>
          </v-container>
        </v-col>
      </v-row>
    </v-container>
  </div> -->
  </v-card>
</template>

<script>
import axios from "axios";
import { mapActions } from "vuex";

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
    ...mapActions(["getWhatwearInfoApi"]),
    goToWhatwearDetail(whatwear) {
      console.log('글번호', whatwear.wear_idx)
      const wearIdx = whatwear.wear_idx
      const nickname = whatwear.nickname
      this.getWhatwearInfoApi({wearIdx, nickname}); // 유저정보 닉네임으로 변경, 현재는 글 작성자로 들어감
      this.$router.push({ name: "WhatWearDetail" });
    },
    getWhatWearList() {
      axios
        .get("http://i4c102.p.ssafy.io:8080/api/wear")
        .then((res) => {
          this.whatwearList = res.data;
          // console.log(res.data);
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