<template>
  <!-- OOTD 디테일 -->
  <v-card elevation="24" max-width="450" class="mx-auto">
    <v-list one-line>
      <v-list-item style="height: 15px">
        <!-- 작성자 정보 -->
        <v-list-item-avatar>
          <v-img src="https://cdn.vuetifyjs.com/images/john.png"></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
          <!-- 닉네임 -->
          <v-list-item-title class="d-inline-block">{{
            getOotdInfo.nickname
          }}</v-list-item-title>
        </v-list-item-content>

        <v-menu bottom>
          <template v-slot:activator="{ on, attrs }">
            <v-avatar v-bind="attrs" v-on="on">
              <v-btn color="black" icon class="d-inline-block">
                <v-icon>mdi-dots-vertical</v-icon>
              </v-btn>
            </v-avatar>
          </template>
          <!-- 수정 및 삭제 버튼 -->
          <v-list>
            <!-- <v-list-item
              v-for="(item, i) in items"
              :key="i"
              @click="goToPage(item)"
            > -->
            <v-list-item>
              <v-list-item-title @click="goToPage('update')"
                >수정</v-list-item-title
              >
            </v-list-item>
            <v-list-item>
              <v-list-item-title @click="goToPage('delete')"
                >삭제</v-list-item-title
              >
            </v-list-item>
          </v-list>
        </v-menu>
      </v-list-item>
    </v-list>
    <!-- OOTD 이미지들 -->
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
        <v-list-item-content>
          <!-- 본문 내용 -->
          <v-list-item-title>{{ getOotdInfo.content }}</v-list-item-title>
          <!-- 해쉬태그 -->
          <v-list-item-subtitle>#ootd #ootd #ootd</v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn icon>
            <v-icon>mdi-heart</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </v-list>
    <DetailComment />
    <OotdUpdate />
    <OotdList />
  </v-card>
</template>

<script>
import DetailComment from "@/components/DetailComment";
import OotdList from "@/components/ootd/OotdList";
import OotdUpdate from "@/components/ootd/OotdUpdate";
// import axios from "axios";
import { mapGetters, mapMutations, mapActions } from "vuex";

export default {
  name: "OotdDetail",
  components: {
    DetailComment,
    OotdList,
    OotdUpdate,
  },
  computed: {
    ...mapGetters(["getOotdInfo"]),
  },
  data() {
    return {
      // ootdInfo: {},
      // items: [
      //   {
      //     text: "수정",
      //   },
      //   {
      //     text: "삭제",
      //   },
      // ],
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
  mounted() {
    // this.getOotdInfoInApi(this.$route.params.no);
    // this.getOotdDetail();
  },
  methods: {
    ...mapMutations(["setOotdInfo"]),
    ...mapActions(["getOotdInfoInApi"]),
    goToPage(item) {
      // 마이페이지, 수정 및 삭제 이동
      if (item.text === "MyPage") {
        this.$router.push("/mypage");
      } else if (item === "update") {
        console.log("in 수정", this.getOotdInfo);

        // this.$router.push({
        //   name: "OotdUpdate",
        //   params: { ootdInfo: this.ootdInfo },
        // });
      }
    },
    goToLogin() {
      // 로그인 페이지로 이동
      this.$router.push("/login");
    },
    // getOotdDetail() {
    //   // Ootd Idx로 디테일 가져옴
    //   const ootdIdx = this.$route.params.no;
    //   axios
    //     .get(`http://i4c102.p.ssafy.io:8080/api/ootd/detail/${ootdIdx}`)
    //     .then((res) => {
    //       // this.ootdInfo = res.data;
    //       this.setOotdInfo(res.data);
    //     })
    //     .catch((err) => {
    //       console.error(err);
    //     });
    // },
  },
};
</script>

<style>
</style>
