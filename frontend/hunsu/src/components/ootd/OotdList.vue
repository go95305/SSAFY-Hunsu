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
      <ImageView :images="ootd.imageUrls" @click.native="goToOotdDetail(ootd)"/>
      <!-- <v-carousel :show-arrows="false" v-if="!ootd.imageUrls">
        <p>loading..</p>
      </v-carousel>
      <v-carousel
        v-else
        :continuous="false"
        :cycle="cycle"
        :show-arrows="true"
        hide-delimiter-background
        delimiter-icon="mdi-minus"
        height="330"
      >
        <v-carousel-item v-for="(imageUrl, i) in ootd.imageUrls" :key="i">
          <v-sheet height="100%" tile>
            <v-row
              class="fill-height"
              align="center"
              justify="center"
              @click="goToOotdDetail(ootd)"
            >
              <v-img :src="imageUrl" />
            </v-row>
          </v-sheet>
        </v-carousel-item>
      </v-carousel> -->
      <v-list two-line>
        <v-list-item>
          <!-- 작성자 프로필 -->
          <v-list-item-avatar>
            <v-img @click="goToProfilePage(getOotdInfo.nickname)" src="https://cdn.vuetifyjs.com/images/john.png"></v-img>
          </v-list-item-avatar>
          <v-list-item-content>
            <v-list-item-title>{{ ootd.ootdContent }}</v-list-item-title>
            <v-list-item-subtitle>
              <!-- 추후 해쉬태그에 검색 링크 걸 예정 -->
              <p
                v-for="(hashtag, i) in ootd.hashtagList"
                :key="i"
                style="display: inline"
              >
                {{ "#" + hashtag }}
              </p>
            </v-list-item-subtitle>
          </v-list-item-content>
          <!-- <v-list-item-content>{{ootd.ootdLike}}개의</v-list-item-content> -->
          <v-list-item-action>
            <!-- 좋아요 버튼 -->
            {{ ootd.ootdLike }}개
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
import { mapActions, mapGetters, mapMutations } from "vuex";
import ImageView from "@/components/module/ImageView";

export default {
  name: "OotdList",
  components: {
    ImageView,
  },
  data() {
    return {
      cycle: false,
      imageUrls: [],
      pageNum: 2,
    };
  },
  computed: { ...mapGetters(["getOotdList", "getNickname", "getOotdInfo"]) },
  created() {
    // let ootdList;
    let root = this;
    this.getOotdListInApi({ sort: 0, pageNum: this.pageNum }).then((res) => {
      res.forEach((info) => {
        root.getImageList({ prefix: "ootd/" + info.ootdIdx }).then((res) => {
          // console.log("hi", res);
          this.getImages({ keys: res }).then((res) => {
            info.imageUrls = res;
            // console.log(info.ootdIdx, " result", res, "info ", info.imageUrls);
          });
        });
      });
    });
  },
  methods: {
    ...mapActions([
      "getOotdInfoInApi",
      "getOotdListInApi",
      "getImages",
      "getImageList",
      "getProfileInfoInApi"
    ]),
    ...mapMutations(["setOotdInfoImages"]),
    goToOotdDetail(ootd) {
      //idx 굳이 보여줄 필요 없을것같아서 params로 변경
      // this.$router.push({ name: "OotdDetail", params: { no: ootd.ootdIdx } });
      let root = this;
      console.log(ootd);
      this.getOotdInfoInApi({
        ootdIdx: ootd.ootdIdx,
        nickname: this.getNickname,
      }).then(() => {
        root
          .getImageList({ prefix: "ootd/" + ootd.ootdIdx })
          .then((res) => {
            console.log("imageList", res);
            root.getImages({ keys: res }).then((res) => {
              console.log("getimages", res);
              root.setOotdInfoImages(res);
            });
          })
          .then(() => {
            this.$router.push({ name: "OotdDetail" });
          });
      });
    },
    goToProfilePage(infoNickname) {
      this.getProfileInfoInApi({
          myNickname: this.getNickname,
          yourNickname: infoNickname,
          }).then(() => {
            this.$router.push({name: "MyPage"})
          }) 
    },
  },
};
</script>