<template>
  <!-- WHATWEAR 메인 페이지 -->
  <!--v-for 사용을 위한 최상위 div-->
  <v-card flat>
    <!-- <v-btn @click="testa">test</v-btn> -->
    <v-card
      v-for="(whatwear, idx) in getWhatwearListInfo"
      :key="idx"
      @click="goToWhatwearDetail(whatwear)"
      flat
    >
      <div class="d-flex align-center pt-4 mb-4">
        <!--프로필사진-->
        <!-- <div class="mt-2"></div> -->
        <v-avatar class="mt-5 ml-2">
          <v-img
            v-if="whatwear.profileImage != null"
            :src="whatwear.profileImage"
            @click="goToWhatwearDetail(whatwear)"
          />
          <!--유저닉네임-->
        </v-avatar>
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
          :length="parseInt(getWhatwearListCount / 10) + 1"
          :total-visible="6"
          @click.native="pageWhatwear()"
        ></v-pagination>
      </div>
    </v-card>
  </v-card>
</template>

<script>
// import { rscApi } from "@/services/api"
import { mapActions, mapMutations, mapGetters } from "vuex";

export default {
  name: "WhatWearList",
  data() {
    return {
      page: 1,
      // length: parseInt(this.getWhatwearListInfo.length / 10) + 1
    };
  },
  computed: {
    ...mapGetters(["getWhatwearListInfo", "getWhatwearListCount"]),
    // test() {
    //   return this.pageWhatwear();
    // },
  },
  async created() {
    await this.getWhatWearList();
  },
  methods: {
    ...mapActions([
      "getWhatwearListInfoApi",
      "getWhatwearInfoApi",
      "getProfiles",
      "getProfileImage",
    ]),
    ...mapMutations(["setWhatwearInfoImages"]),
    goToWhatwearDetail(whatwear) {
      // console.log('글번호', whatwear.wear_idx)
      const wearIdx = whatwear.wear_idx;
      const voteCheck = whatwear.voteActivated;
      this.getWhatwearInfoApi({ wearIdx, voteCheck }) // 유저정보 닉네임으로 변경, 현재는 글 작성자로 들어감
        .then(() => {
          this.getProfileImage({
            nickname: whatwear.nickname,
            target: "target",
          });
          this.getImageList({ prefix: "whatwear/" + wearIdx })
            .then((res) => {
              this.setWhatwearInfoImages(res);
            })
            .then(() => {
              this.getProfileImage({ uid: whatwear.uid, target: "target" });
            })
            .then(() => {
              this.$router.push({ name: "WhatWearDetail" });
            });
        });
    },
    getWhatWearList() {
      const pageNum = 1;
      this.getWhatwearListInfoApi(pageNum);
      // let root = this;
      // rscApi
      //   .get(`wear/${pageNum}`)
      //   .then((res) => {
      //     this.whatwearList = res.data.wearMainDTOList;
      //     this.length = parseInt(res.data.count / 10) + 1;

      //     this.getProfiles(this.whatwearList);
      //   })
      //   .then(() => {
      //     root.$foreceUpdate();
      //   })
      //   .catch((err) => {
      //     console.error(err);
      //   });
    },
    async pageWhatwear() {
      const pageNum = this.page;
      this.getWhatwearListInfoApi(pageNum);
      // await rscApi
      //   .get(`wear/${pageNum}`)
      //   .then((res) => {
      //     this.whatwearList = res.data.wearMainDTOList;
      //     // console.log('test', res)
      //     this.getProfiles(this.whatwearList);
      //   })
      //   .then(() => this.$foreceUpdate())
      //   .catch((err) => {
      //     console.error(err);
      //   });
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