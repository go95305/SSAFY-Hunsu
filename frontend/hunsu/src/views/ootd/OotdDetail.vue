<template>
  <!-- OOTD 디테일 -->
  <v-card elevation="24" class="mx-auto">
    <v-list one-line>
      <v-list-item style="height: 15px">
        <!-- 작성자 정보 -->
        <v-list-item-avatar>
          <v-img
            :src="getTargetProfileImage"
            @click="goToProfilePage(getOotdInfo.nickname)"
          ></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
          <!-- 닉네임 -->
          <v-list-item-title class="d-inline-block">{{
            getOotdInfo.nickname
          }}</v-list-item-title>
        </v-list-item-content>

        <v-menu bottom v-if="getOotdInfo.nickname === getNickname">
          <template v-slot:activator="{ on, attrs }">
            <v-avatar v-bind="attrs" v-on="on">
              <v-btn color="black" icon class="d-inline-block">
                <v-icon>mdi-dots-vertical</v-icon>
              </v-btn>
            </v-avatar>
          </template>
          <!-- 수정 및 삭제 버튼 -->
          <v-list>
            <v-list-item>
              <v-list-item-title @click="onoffUpdateDialog()">
                수정
              </v-list-item-title>
            </v-list-item>
            <v-list-item>
              <v-list-item-title @click="onoffDeleteDialog()" 
                >삭제</v-list-item-title
              >
            </v-list-item>
          </v-list>
        </v-menu>
      </v-list-item>
    </v-list>
    <!-- OOTD 이미지들 -->
    <v-carousel :show-arrows="false" v-if="!getOotdInfoImages">
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
      <v-carousel-item v-for="(imageUrl, i) in getOotdInfoImages" :key="i">
        <v-sheet height="100%" tile>
          <v-row class="fill-height" align="center" justify="center">
            <!-- 사진 클릭 시 이미지 업로드 -->
            <v-img :src="imageUrl" />
          </v-row>
        </v-sheet>
      </v-carousel-item>
    </v-carousel>
    <!-- 이미지 하단 본문 및 해쉬태그 -->
    <v-list two-line>
      <v-list-item>
        <v-list-item-content>
          <!-- 본문 내용 -->
          <v-list-item-title style="white-space: normal">{{
            getOotdInfo.content
          }}</v-list-item-title>
          <!-- ### Hashtag -->
          <v-list-item-subtitle>
            <!-- 추후 해쉬태그에 검색 링크 걸 예정 -->
            <p
              v-for="(hashtag, i) in getOotdInfo.hashtagList"
              :key="i"
              style="display: inline"
            >
              {{ "#" + hashtag }}
            </p>
          </v-list-item-subtitle>
        </v-list-item-content>
        <!-- ### 좋아요 button -->
        <v-list-item-action>
          <v-btn icon @click="toggleLikeInDetail">
            <v-icon v-model="iconName" color="red">{{ iconName }}</v-icon>
            <!-- <v-icon v-model="iconName" v-else>{{ iconName }}</v-icon> -->
            <div>{{ getOotdInfo.likeCount }}</div>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </v-list>

    <!-- ### Delete Dialog -->
    <v-dialog
      v-model="deleteDialog"
      hide-overlay
      transition="dialog-bottom-transition"
    >
      <v-card>
        <v-card-title class="mb-3 font-weight-bold">해당 글을 삭제하시겠습니까?</v-card-title>
        <v-card-subtitle>사진과 글이 삭제됩니다.</v-card-subtitle>
        <v-spacer></v-spacer>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn class="font-weight-bold" color="primary" text @click="onoffDeleteDialog"> 취소 </v-btn>
          <v-btn class="font-weight-bold" color="error" text @click="deleteOotd"> 삭제 </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- ### Update Dialog -->
    <v-dialog
      v-model="updateDialog"
      fullscreen
      hide-overlay
      transition="dialog-bottom-transition"
    >
      <v-card>
        <!-- 카드 상단 -->
        <v-toolbar dark color="black">
          <!-- 닫힘버튼 -->
          <v-btn icon dark @click="onoffUpdateDialog">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>OOTD 수정</v-toolbar-title>
          <v-spacer></v-spacer>
          <!-- 작성완료 버튼 -->
          <v-toolbar-items>
            <v-btn dark text @click="updateOotd()"> 수정 완료 </v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <!-- 카드 본문 -->
        <v-list three-line subheader>
          <v-subheader>사진 등록</v-subheader>
          <input ref="imageInput" type="file" hidden @change="onChangeImages" />
          <v-btn class="mx-5" type="button" @click="onClickImageUpload"
            >사진 업로드</v-btn
          >
          <v-img
            class="mx-5 my-5"
            v-if="imageUrl"
            :src="imageUrl"
            width="100"
          ></v-img>
        </v-list>
        <v-divider></v-divider>
        <v-list three-line subheader>
          <div>
            <!-- 본문 입력 -->
            <v-textarea
              v-model="updateOotdContent"
              clearable
              clear-icon="mdi-close-circle"
              :rules="[rules.required, rules.min, rules.contentMax]"
              counter="300"
              label="내용"
              class="px-5"
              >{{ updateOotdContent }}</v-textarea
            >
            <!-- 해쉬태그 입력 -->
            <v-text-field
              label="해시태그 추가"
              @keydown.enter="addHashtag()"
              v-model="updateOotdHashtag"
              class="px-5"
            ></v-text-field>
            <div>
              <v-chip
                v-for="(hashtag, idx) in updateOotdHashtagArray"
                :key="idx"
                class="ma-2"
                close
                color="red"
                text-color="white"
                @click="deleteHashtag(hashtag)"
              >
                {{ hashtag }}
              </v-chip>
            </div>
          </div>
        </v-list>
      </v-card>
    </v-dialog>
    <!-- 댓글 -->
    <OotdDetailComment />
    <!-- 디테일 하단 리스트 -->
    <!-- <OotdList /> -->
  </v-card>
</template>

<script>
import OotdDetailComment from "@/components/ootd/OotdDetailComment";
// import OotdList from "@/components/ootd/OotdList";

import { mapGetters, mapMutations, mapActions } from "vuex";

export default {
  name: "OotdDetail",
  components: {
    OotdDetailComment,
    // OotdList,
    // OotdUpdate,
  },
  computed: {
    ...mapGetters([
      "getOotdInfo",
      "getNickname",
      "getOotdInfoImages",
      "getTargetProfileImage",
    ]),
  },
  data() {
    return {
      dialog: false,
      updateDialog: false, // 수정창 dialog 활성화
      deleteDialog: false, // 삭제창 dialog 활성화
      required: false,
      cycle: false,
      notifications: false,
      sound: true,
      widgets: false,
      rules: {
        // 입력 창 체크 변수
        required: (v) => !!v || "Required",
        min: (v) => v.trim().length > 0 || "공백안됨",
        contentMax: (v) => v.length <= 300 || "300자이하",
      },
      imageUrl: null,
      updateOotdHashtag: "", // 수정 관련 변수들
      updateOotdContent: "",
      updateOotdHashtagArray: [],
      iconName: "", // 좋아요 토글 변수
    };
  },
  watch: {
    getOotdInfo: (newData) => {
      if (newData.likeChk) {
        return "mdi-heart";
      } else {
        return "mdi-heart-outline";
      }
    },
  },
  mounted() {
    // console.log("mounted");
    if (this.getOotdInfo.likeChk) {
      // 좋아요 초기 설정 부분
      this.iconName = "mdi-heart"; // 꽉찬 하트
    } else {
      this.iconName = "mdi-heart-outline"; // 덜찬 하트
    }
  },
  methods: {
    ...mapMutations([
      "setOotdInfo",
      "setTargetProfileImage",
      "setOotdInfoImages",
    ]),
    ...mapActions([
      "getOotdInfoInApi",
      "updateOotdInfo",
      "deleteOotdInfo",
      "toggleLike",
      "getProfileInfoInApi",
      "getImageList",
      "getProfileImage",
      "getOotdListInApi",
      "getProfiles",
    ]),
    onoffUpdateDialog() {
      // 수정 dialog 활성화
      // console.log("good");
      this.updateDialog = !this.updateDialog;
      this.updateOotdContent = this.getOotdInfo.content;
      this.updateOotdHashtagArray = this.getOotdInfo.hashtagList.slice();
    },
    onoffDeleteDialog() {
      // 삭제 dialog 활성화
      this.deleteDialog = !this.deleteDialog;
    },
    goToProfilePage(infoNickname) {
      // 타겟 프로필 페이지로 이동
      // let root = this;
      this.getProfileInfoInApi({
        // 타겟의 프로필 정보 가졍괴
        myNickname: this.getNickname,
        yourNickname: infoNickname,
      }).then(() => {
        this.getProfileImage({
          // 타겟의 프로필 사진 가져오기
          nickname: infoNickname,
          target: "target",
        });
        this.$router.push({ name: "MyPage" });
      });
    },
    goToLogin() {
      // 로그인 페이지로 이동
      this.$router.push("/login");
    },

    //Delete 관련 Functions
    deleteOotd() {
      const result = this.deleteOotdInfo(this.getOotdInfo.ootdIdx);
      if (result) {
        this.$router.push("/ootd");
      } else {
        console.log("삭제실패");
      }
    },
    //Update 관련 Functions
    onClickImageUpload() {
      this.$refs.imageInput.click();
    },
    onChangeImages(e) {
      console.log(e.target.files);
      const file = e.target.files[0];
      this.imageUrl = URL.createObjectURL(file);
    },
    addHashtag() {
      // 태그 입력창 엔터 시 실행
      this.updateOotdHashtagArray.push(this.updateOotdHashtag);
      this.updateOotdHashtag = "";
    },
    deleteHashtag(hashtag) {
      // 태그 클릭 시 실행
      const index = this.updateOotdHashtagArray.indexOf(hashtag);
      this.updateOotdHashtagArray.splice(index, 1);
    },
    updateOotd() {
      // 작성완료 클릭 시 실행
      this.updateDialog = false;
      this.updateOotdInfo({
        content: this.updateOotdContent,
        hashtagList: this.updateOotdHashtagArray,
        ootdIdx: this.getOotdInfo.ootdIdx,
      });
    },
    toggleLikeInDetail() {
      if (this.getOotdInfo.likeChk) {
        // 좋아요 였다가 좋아요 해제로
        this.iconName = "mdi-heart-outline";
        this.getOotdInfo.likeCount -= 1;
      } else {
        // 좋아요 해제였다가 좋아요로
        this.iconName = "mdi-heart";
        this.getOotdInfo.likeCount += 1;
      }
      this.toggleLike(this.getNickname);
    },
  },
};
</script>

<style>
</style>
