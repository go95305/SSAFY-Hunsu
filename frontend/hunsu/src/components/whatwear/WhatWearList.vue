<template>
  <!-- WHATWEAR 메인 페이지 -->
  <!--v-for 사용을 위한 최상위 div-->
  <v-card flat>
    <v-btn @click="testa">test</v-btn>
    <v-card
      v-for="(whatwear, idx) in whatwearList"
      :key="idx"
      @click="goToWhatwearDetail(whatwear)"
      flat
    >
      <div class="d-flex align-center pt-4 mb-4">
        <!--프로필사진-->
        <div>
          <v-avatar class="ml-2">
            <v-img
              v-if="whatwear.profileImage"
              :src="whatwear.profileImage"
              alt="John"
              id="profile"
            />
            <v-img
              v-else
              src="https://s.pstatic.net/mimgnews/image/upload/office_logo/018/2017/01/05/logo_018_18_20170105111205.png?type=nf40_40"
            />
          </v-avatar>
        </div>
        <div class="ml-2">
          <div>
            <div class="d-flex">
              <!--뭘입을까 글제목-->
              <v-card-subtitle class="font-weight-bold" id="title">
                {{ whatwear.title }}
              </v-card-subtitle>
              <!--투표기능뱃지-->
              <v-badge
                v-if="whatwear.voteActivated"
                color="red accent-3"
                content="투표"
                inline
                class="ml-2"
              >
              </v-badge>
            </div>
            <!--유저닉네임-->
            <v-card-subtitle class="font-weight-light text-caption" id="nickname">
              {{ whatwear.nickname }}
            </v-card-subtitle>
          </div>

        </div>
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
      return this.pageWhatwear();
    },
  },
  async created() {
    await this.getWhatWearList();
  },
  methods: {
    ...mapActions(["getWhatwearInfoApi", "getProfiles", "getProfileImage"]),
    goToWhatwearDetail(whatwear) {
      // console.log('글번호', whatwear.wear_idx)
      const wearIdx = whatwear.wear_idx;
      const nickname = whatwear.nickname;
      this.getWhatwearInfoApi({ wearIdx, nickname }) // 유저정보 닉네임으로 변경, 현재는 글 작성자로 들어감
        .then(() => {
          this.getProfileImage({
            nickname: nickname,
            target: "target",
          });
        });
      this.$router.push({ name: "WhatWearDetail" });
    },
    getWhatWearList() {
      const pageNum = 1;
      let root = this;
      axios
        .get(`http://i4c102.p.ssafy.io:8080/api/wear/${pageNum}`)
        .then((res) => {
          this.whatwearList = res.data.wearMainDTOList;
          this.length = parseInt(res.data.count / 10) + 1;

          this.getProfiles(this.whatwearList);
        })
        .then(() => {
          root.$foreceUpdate();
        })
        .catch((err) => {
          console.error(err);
        });
    },
    async pageWhatwear() {
      // console.log(this.page)
      const pageNum = this.page;
      await axios
        .get(`http://i4c102.p.ssafy.io:8080/api/wear/${pageNum}`)
        .then((res) => {
          this.whatwearList = res.data.wearMainDTOList;
          // console.log('test', res)
          this.getProfiles(this.whatwearList);
        })
        .then(() => this.$foreceUpdate())
        .catch((err) => {
          console.error(err);
        });
    },
    testa() {
      this.whatwearList.map((info) => {
        console.log(info.profileImage);
      });
    },
  },
};
</script>

<style>
#profile {
  width: 35px;
  height: 35px;
}

#nickname {
  padding: 0 0 0 0;
}

#title {
  padding: 0 0 0 0;
}
</style>