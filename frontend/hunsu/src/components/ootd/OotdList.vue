<template>
  <!-- OOTD 메인 페이지 -->
  <div>
    <!-- OOTD 하나 클릭하면 디테일페이지 뜨는 섹션 -->
    <!-- <router-view></router-view> -->
    <v-card
      v-for="(ootd, idx) in getOotdList"
      :key="idx"
      elevation="24"
      max-width="450"
      class="mx-auto my-5"
    >
      <!-- OOTD 사진 -->
      <v-carousel
        :continuous="false"
        :cycle="cycle"
        :show-arrows="false"
        hide-delimiter-background
        delimiter-icon="mdi-minus"
        height="330"
      >
        <v-carousel-item v-for="(imageUrl, i) in imageUrls" :key="i">
          <v-sheet height="100%" tile>
            <v-row
              class="fill-height"
              align="center"
              justify="center"
              @click="goToOotdDetail(ootd)"
            >
              <v-img :src="imageUrl" />
              <!-- <div class="display-3">Slide</div> -->
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
          <!-- <v-list-item-content>{{ootd.ootdLike}}개의</v-list-item-content> -->
          <v-list-item-action>
            <!-- 좋아요 버튼 -->
            {{ ootd.ootdLike }}개의
            <v-btn icon class="mr-1">
              <v-icon color="red">mdi-heart</v-icon>
            </v-btn>
          </v-list-item-action>
        </v-list-item>
      </v-list>
    </v-card>
  </div>
</template>


<script>
// import axios from "axios";
import { mapActions, mapGetters } from "vuex";

export default {
  name: "OotdList",
  data() {
    return {
      colors: [
        "green",
        "secondary",
        "yellow darken-4",
        "red lighten-2",
        "orange darken-1",
      ],
      cycle: false,
      slides: ["First", "Second", "Third", "Fourth", "Fifth"],
      imageUrls: [],
    };
  },
  computed: { ...mapGetters(["getOotdList", "getNickname"]) },
  created() {
    // let ootdList;
    // let getImages = this.getImages;
    let root = this;
    // console.log("this", root);
    // this.getImages({ key: "ootd/60", num: 3 });
    // let images = [];
    this.getOotdListInApi(0).then((res) => {
      // console.log(res);
      res.forEach((info) => {
        if (info.ootdIdx === 60) {
          root.getImageList({ prefix: "ootd/" + info.ootdIdx }).then((res) => {
            console.log(res);
            this.getImages({ keys: res }).then((res) => {
              root.imageUrls = res;
              console.log("result", res);
            });
          });
        }
      });
      // console.log("in mount", root.imageUrls);
      // root.imageUrls = images;
    });
    // console.log("out", root.imageUrls);
    // console.log(this.getOotdList);
  },
  methods: {
    ...mapActions([
      "getOotdInfoInApi",
      "getOotdListInApi",
      "getImages",
      "getImageList",
    ]),

    goToOotdDetail(ootd) {
      //idx 굳이 보여줄 필요 없을것같아서 params로 변경
      // this.$router.push({ name: "OotdDetail", params: { no: ootd.ootdIdx } });
      this.getOotdInfoInApi({
        ootdIdx: ootd.ootdIdx,
        nickname: this.getNickname,
      }).then(() => {
        this.$router.push({ name: "OotdDetail" });
      });
    },
  },
};
</script>