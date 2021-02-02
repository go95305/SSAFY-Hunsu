<template>
  <!-- OOTD 메인 페이지 -->
  <div>
    <!-- OOTD 하나 클릭하면 디테일페이지 뜨는 섹션 -->
    <!-- <router-view></router-view> -->
    <v-card
      v-for="(ootd, idx) in ootdList"
      :key="idx"
      elevation="24"
      max-width="450"
      class="mx-auto"
      @click="goToOotdDetail(ootd)"
    >
      <v-system-bar lights-out></v-system-bar>
      <!-- OOTD 사진 -->
      <v-carousel
        :continuous="false"
        :cycle="cycle"
        :show-arrows="false"
        hide-delimiter-background
        delimiter-icon="mdi-minus"
        height="330"
      >
        <v-carousel-item v-for="(slide, i) in slides" :key="i">
          <v-sheet :color="colors[i]" height="100%" tile>
            <v-row class="fill-height" align="center" justify="center">
              <div class="display-3">{{ slide }} Slide</div>
            </v-row>
          </v-sheet>
        </v-carousel-item>
      </v-carousel>
      <v-list two-line>
        <v-list-item>
          <!-- 작성자 프로필 -->
          <v-list-item-avatar>
            <v-img src="https://cdn.vuetifyjs.com/images/john.png"></v-img>
          </v-list-item-avatar>
          <v-list-item-content>
            <v-list-item-title>{{ ootd.ootdContent }}</v-list-item-title>
            <v-list-item-subtitle>{{ ootd.nickname }}</v-list-item-subtitle>
          </v-list-item-content>
          <v-list-item-action>
            <!-- 좋아요 버튼 -->
            <v-btn icon>
              <v-icon>mdi-heart</v-icon>
            </v-btn>
          </v-list-item-action>
        </v-list-item>
      </v-list>
    </v-card>
  </div>
</template>


<script>
import axios from "axios";
import { mapActions } from "vuex";

export default {
  name: "OotdList",
  data() {
    return {
      ootdList: [],
      colors: [
        "green",
        "secondary",
        "yellow darken-4",
        "red lighten-2",
        "orange darken-1",
      ],
      cycle: false,
      slides: ["First", "Second", "Third", "Fourth", "Fifth"],
    };
  },
  created() {
    this.getOotdList();
  },
  methods: {
    ...mapActions(["getOotdInfoInApi"]),
    getOotdList() {
      axios
        .get("http://i4c102.p.ssafy.io:8080/api/ootd/0")
        .then((res) => {
          this.ootdList = res.data;
          console.log("ootd List ", this.ootdList);
        })
        .catch((err) => {
          console.error(err);
        });
    },
    goToOotdDetail(ootd) {
      // console.log(ootd);
      // console.log(ootd.ootdIdx);
      // this.$router.push(`ootd/detail?${ootd.ootdIdx}`);

      //idx 굳이 보여줄 필요 없을것같아서 params로 변경
      // this.$router.push({ name: "OotdDetail", params: { no: ootd.ootdIdx } });
      this.getOotdInfoInApi(ootd.ootdIdx);
      this.$router.push({ name: "OotdDetail" });
    },
  },
};
</script>