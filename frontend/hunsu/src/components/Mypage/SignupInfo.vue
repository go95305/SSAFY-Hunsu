<template>
  <!-- 회원가입 후 추가정보 입력할 컴포넌트 -->
  <div>
    <v-toolbar dark color="black">
      <v-toolbar-title class="text-subtitle-1">회원가입</v-toolbar-title>
    </v-toolbar>
    <!-- 프로필 사진 -->
    <div id="profile_image">
      <v-avatar width="100px" height="100px">
        <v-img
          v-if="getUploadImageUrls.length !== 0"
          :src="getUploadImageUrls[getUploadImageUrls.length - 1]"
        />
        <!-- <v-img v-else-if="getMyProfileImage" :src="getMyProfileImage" /> -->
        <v-img
          v-else
          src="@/assets/profilephoto.png" 
          alt="John"
        />
      </v-avatar>
      <!-- 프로필 이미지 업로드 부분 -->
      <!-- <v-file-input truncate-length="15" hide-input></v-file-input> -->
      <input ref="imageInput" type="file" hidden @change="onChangeImages" />
      <div class="display: inline-block;">
        <v-btn class="mx-5 my-2" type="button" @click="onClickImageUpload"
          >사진 업로드</v-btn
        >
      </div>
    </div>
    <!-- <v-avatar>
      <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" />
    </v-avatar>
    <v-row align="center" justify="space-around"> -->
    <!-- 프로필 사진 변경 버튼 -->
    <!-- <v-btn tile color="success"> Edit </v-btn>
    </v-row> -->
    <!-- 닉네임 입력 -->
    <v-text-field
      class="mx-auto mb-7"
      label="닉네임"
      :rules="rules"
      hide-details="auto"
      style="width: 350px"
      v-model="nickname"
      outlined
    ></v-text-field>
    <v-divider></v-divider>
    <h3 class="ml-3 mt-5 mb-4">추가정보(선택)</h3>
    <!-- 키 입력, 사이즈 선택 , 숫자만 입력하게 값검증 필요-->
    <v-text-field
      class="mx-auto"
      label="키(cm)"
      hide-details="auto"
      style="width: 350px"
      v-model="height"
      outlined
    ></v-text-field>
    <v-container fluid>
      <v-row align="center">
        <v-col class="d-flex" cols="12" sm="6">
          <v-select
            style="width: 350px"
            outlined
            v-model="size"
            :items="items"
            label="사이즈"
          ></v-select>
        </v-col>
      </v-row>
    </v-container>
    <v-btn depressed class="" x-large style="width: 375px" dark @click="signUp"
      >회원가입</v-btn
    >
  </div>
</template>

<script>
import { mapActions, mapGetters, mapMutations } from "vuex";
// import axios from "axios";

export default {
  name: "SignupInfo",

  props: {
    accessToken: {
      type: String,
    },
    refreshToken: {
      type: String,
    },
  },
  data: () => ({
    nickname: "",
    height: "",
    size: "",
    rules: [
      (value) => !!value || "Required.",
      (value) => (value || "").length <= 20 || "Max 20 characters",
      // (value) => {
      // const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      // return pattern.test(value) || "Invalid e-mail.";
      // },
    ],
    items: ["XXS", "XS", "S", "M", "L", "XL", "XXL"],
  }),
  mounted() {
    // if (this.accessToken == null || this.refreshToken == null) {
    //   //회원가입 창에서 새로고침 시 홈으로 이동 처리 (카카오 액세스 토큰을 따로 저장하지 않아 새로고침하면 무효회됨)
    //   //vuex에 저장하면되지만, 서버측에선 카카오 액세스토큰 관리를 전적으로 서버에서 하길 원해, 저장 비권장
    //   this.$router.push("/");
    // }
    console.log("signup mount", this.accessToken, this.refreshToken);
  },
  computed: {
    ...mapGetters([
      "getAccessToken",
      "getUploadImageFiles",
      "getUploadImageUrls",
      "getUid",
    ]),
  },
  methods: {
    ...mapActions(["signUpInApi", "kakaoLogin", "uploadProfile"]),
    ...mapMutations([
      "setAllToken",
      "setUploadImageFiles",
      "setUploadImageUrls",
      "clearUploads",
    ]),
    async signUp() {
      console.log(this.nickname, this.height, this.size);
      await this.signUpInApi({
        accessToken: this.accessToken,
        height: this.height,
        nickname: this.nickname,
        size: this.size,
      });
      if (this.getUploadImageFiles.length !== 0) {
        await this.uploadProfile();
        this.clearUploads();
      }
      console.log("in signupinfo 2");
      alert("회원가입 감사합니다!!");
      this.$router.push("/");
      // this.kakaoLogin();
    },
    onClickImageUpload() {
      this.$refs.imageInput.click();
    },
    onChangeImages(e) {
      console.log(e.target.files);

      this.setUploadImageFiles(e.target.files);
      this.setUploadImageUrls();
      console.log("onChange imageURl ", this.getUploadImageUrls);
    },
  },
};
</script>

<style>
</style>