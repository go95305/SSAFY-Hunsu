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
      <div class="d-flex align-center pt-4 mb-4">
        <!--프로필사진-->
        <!-- <div class="mt-2"></div> -->
        <v-avatar class="mt-5 ml-2">
          <v-img
            :src="whatwear.profileImage"
            @click="goToWhatwearDetail(whatwear)"
          />
          <!-- <v-img
            v-else
            src="https://s.pstatic.net/static/newsstand/2020/logo/dark/0604/018.png"
          /> -->
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
      whatwearList: [],
      // length: parseInt(this.getWhatwearListInfo.length / 10) + 1
    };
  },
  computed: {
    ...mapGetters(["getWhatwearListInfo", "getWhatwearListCount"]),
  },
  async mounted() {
    await this.pageWhatwear();
  },
  methods: {
    ...mapActions([
      "getWhatwearListInfoApi",
      "getWhatwearInfoApi",
      "getProfileImage",
      "getImageList",
      "getWhatwearProfile",
    ]),
    ...mapMutations(["setWhatwearInfoImages"]),
    async goToWhatwearDetail(whatwear) {
      console.log("go detail", whatwear);
      // 뭘입을까 상세정보 가져오기
      await this.getWhatwearInfoApi({
        wearIdx: whatwear.wear_idx,
        voteCheck: whatwear.voteActivated,
      });

      // 이미지 업로드 및 세팅
      const images = await this.getImageList({
        prefix: "whatwear/" + whatwear.wear_idx,
      });
      this.setWhatwearInfoImages(images);

      // 작성자 프로필사진 가져오기
      await this.getProfileImage({ uid: whatwear.uid, target: "target" });
      this.$router.push({ name: "WhatWearDetail" });
    },

    async pageWhatwear() {
      // durldrudlrudrljdruldrldrldlr
      await this.getWhatwearListInfoApi(this.page);
      this.whatwearList = this.getWhatwearListInfo;
      this.getImages();
      // await this.getWhatwearListInfo.forEach(async (info) => {
      //   const profile = await this.getWhatwearProfile(info.uid);
      //   this.$set(info, "profileImage", profile);
      // });
      console.log(this.getWhatwearListInfo);
    },
    getImages() {
      this.whatwearList.forEach(async (info) => {
        const profile = await this.getWhatwearProfile(info.uid);
        console.log(profile);
        this.$set(info, "profileImage", profile);
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