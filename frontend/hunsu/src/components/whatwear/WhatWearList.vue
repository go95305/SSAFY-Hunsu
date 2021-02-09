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
        <v-avatar class="mt-5 ml-2">
          <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" id="profile"/>
        </v-avatar>
        <!--유저닉네임-->
        <v-card-subtitle class="mt-4 font-weight-bold" id="nickname">
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
          class="mt-8"
        >
        </v-badge>
        <!-- <div class="mb-2"></div> -->
      </div>
    </v-card>
    <v-card flat class="mt-4">
      <div class="text-center">
        <v-pagination
          v-model="page"
          :length="length"
          :total-visible="6"
          @click.native="pageWhatwear()"
        ></v-pagination>
      </div>
    </v-card>
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
      page: 1,
      length: 0,

    };
  },
  computed: {
    test() {
      return this.pageWhatwear()
    }
  },
  created() {
    this.getWhatWearList();
  },
  methods: {
    ...mapActions(["getWhatwearInfoApi"]),
    goToWhatwearDetail(whatwear) {
      // console.log('글번호', whatwear.wear_idx)
      const wearIdx = whatwear.wear_idx
      const nickname = whatwear.nickname
      this.getWhatwearInfoApi({wearIdx, nickname}); // 유저정보 닉네임으로 변경, 현재는 글 작성자로 들어감
      this.$router.push({ name: "WhatWearDetail" });
    },
    getWhatWearList() {
      const pageNum = 1
      axios
        .get(`http://i4c102.p.ssafy.io:8080/api/wear/${pageNum}`)
        .then((res) => {
          this.whatwearList = res.data.wearMainDTOList;
          this.length = parseInt(res.data.count / 10) + 1
          // console.log(res.data);
        })
        .catch((err) => {
          console.error(err);
        });
    },
    pageWhatwear() {
      // console.log(this.page)
      const pageNum = this.page
      axios
        .get(`http://i4c102.p.ssafy.io:8080/api/wear/${pageNum}`)
        .then((res) => {
          this.whatwearList = res.data.wearMainDTOList;
          // console.log('test', res)
        })
        .catch((err) => {
          console.error(err)
        })
    }
  },
};
</script>

<style>
#profile {
  width: 35px;
  height: 35px;
}

#nickname {
  padding-left: 5px;
  padding-right: 5px;
}
</style>