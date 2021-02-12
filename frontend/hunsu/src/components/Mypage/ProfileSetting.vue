<template>
  <v-dialog
    v-model="dialog"
    fullscreen
    hide-overlay
    transition="dialog-bottom-transition"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-btn icon v-bind="attrs" v-on="on">
        <v-icon>mdi-cog-outline</v-icon>
      </v-btn>
    </template>
    <v-card>
      <v-toolbar dark color="dark">
        <v-btn icon dark @click="dialog = false">
          <v-icon>mdi-close</v-icon>
        </v-btn>
        <v-toolbar-title>프로필수정</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-toolbar-items>
          <v-btn dark text @click="updateProfile(getNickname, newNickname, height, size)"> 완료 </v-btn>
        </v-toolbar-items>
      </v-toolbar>
      <div id="profile_image">
        <v-avatar width="100px" height="100px">
          <v-img
            v-if="getUploadImageUrls.length !== 0"
            :src="getUploadImageUrls[0]"
          />
          <v-img v-else-if="getMyProfileImage" :src="getMyProfileImage" />
          <v-img
            v-else
            src="https://cdn.vuetifyjs.com/images/john.jpg"
            alt="John"
          />
        </v-avatar>
        <!-- 프로필 이미지 업로드 부분 -->
        <!-- <v-file-input truncate-length="15" hide-input></v-file-input> -->
        <input ref="imageInput" type="file" hidden @change="onChangeImages" />
        <div class="display: inline-block;">
          <v-btn class="mx-5" type="button" @click="onClickImageUpload"
            >사진 업로드</v-btn
          >
        </div>
      </div>
      <div id="info_input">
        <v-text-field
          :label="getNickname"
          v-model="newNickname"
          hint="닉네임 중복불가안내, 규칙안내"
          outlined
        ></v-text-field>
        <p class="text-h6 font-weight-bold">추가정보</p>
        <p class="text-subtitle2">추천서비스 제공에 쓰이는 정보입니다.</p>
        <!-- 숫자만 가능하게 필터링 -->
        <v-text-field
          :label="getMyProfileInfo.height"
          v-model="height"
          hint="cm를 제외하고 적어주세요"
          outlined
        ></v-text-field>
        <v-select :items="items" v-model="size" :label="getMyProfileInfo.size" outlined></v-select>
      </div>
    </v-card>
  </v-dialog>
</template>
<script>
import { mapActions, mapGetters, mapMutations } from "vuex";
export default {
  name: "ProfileSetting",

  computed: {
    ...mapGetters([
      "getMyProfileImage",
      "getUploadImageFiles",
      "getUploadImageUrls",
      "getNickname",
      "getMyProfileInfo",
    ]),
  },

  data() {
    return {
      dialog: false,
      notifications: false,
      sound: true,
      widgets: false,
      items: ["XS", "S", "M", "L", "XL", "XXL"],
      newNickname: "",
      height: "",
      size: "",
    }
  },
  methods: {
    ...mapActions(["uploadProfile", "getProfileImage", "updateMyProfileInfoInApi", "getProfileInfoInApi"]),
    ...mapMutations([
      "setUploadImageFiles",
      "setUploadImageUrls",
      "clearUploads",
      "setTargetProfileImage",
    ]),
    onClickImageUpload() {
      this.$refs.imageInput.click();
    },
    onChangeImages(e) {
      console.log(e.target.files);

      this.setUploadImageFiles(e.target.files);
      this.setUploadImageUrls();
      console.log("onChange imageURl ", this.getUploadImageUrls);
    },
    updateProfile(getNickname, newNickname, height, size) {
      // 프로필사진 업로드, 정보수정
      this.dialog = false;
      this.uploadProfile()
        .then(() => {
          this.getProfileImage({
            nickname: this.getNickname,
            target: "my",
          });
          this.setTargetProfileImage(this.getMyProfileImage);
        })
        .then(() => {
          this.clearUploads();
          this.$forceUpdate();
        });
      this.updateMyProfileInfoInApi({getNickname, newNickname, height, size})
      .then(() => {
        console.log("수정후내정보", this.getMyProfileInfo)
        const myNickname = newNickname
        const yourNickname = newNickname
        this.getProfileInfoInApi({myNickname, yourNickname})
        console.log('겟성공?')
        this.$router.push({name: "Home"}).catch(() => {})


      })
    },
  },
};
</script>

<style>
#profile_image {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}
#info_input {
  margin: auto;
  width: 365px;
}
</style>