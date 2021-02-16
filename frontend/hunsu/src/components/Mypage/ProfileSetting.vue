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
          <v-btn :disabled="nicknameStatus === 'nope' " dark text @click="updateProfile(newNickname, height, size)">
            완료
          </v-btn>
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
          <v-btn class="mx-5 my-2" type="button" @click="onClickImageUpload"
            >사진 업로드</v-btn
          >
        </div>
      </div>
      <div id="info_input">
        <div class="mt-3">
          <v-text-field
            class="d-inline-block mx-5"
            :label="getNickname"
            v-model="newNickname"
            hint="닉네임 중복체크 필수"
            outlined
          ></v-text-field>
          <v-btn :disabled="getNickname === newNickname" class="d-inline-block" @click="nicknameCheck(newNickname)">중복체크</v-btn>
        </div>
        <div class="mx-5">
          <p class="text-h6 font-weight-bold">추가정보</p>
          <p class="text-subtitle2">추천서비스 제공에 쓰이는 정보입니다.</p>
          <!-- 숫자만 가능하게 필터링 -->
          <v-text-field
            label="키"
            v-model="height"
            :placeholder="String(getMyProfileInfo.height)"
            hint="cm를 제외하고 적어주세요"
            outlined
          ></v-text-field>
          <v-select
            :items="items"
            v-model="size"
            :label="getMyProfileInfo.size"
            outlined
          ></v-select>
        </div>
        <div class="d-flex justify-end mt-15 pt-15">
          <div>
          <v-btn text @click="deleteUser()">회원탈퇴</v-btn>
          </div>
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>
<script>
import { mapActions, mapGetters, mapMutations } from "vuex";
import { rscApi, authApi } from '@/services/api';

export default {
  name: "ProfileSetting",

  computed: {
    ...mapGetters([
      "getMyProfileImage",
      "getUploadImageFiles",
      "getUploadImageUrls",
      "getNickname",
      "getMyProfileInfo",
      "getUid",
    ]),
  },

  data() {
    return {
      dialog: false,
      notifications: false,
      sound: true,
      widgets: false,
      items: ["XXS", "XS", "S", "M", "L", "XL", "XXL"],
      newNickname: "",
      height: "",
      size: "",
      nicknameStatus: '',
    };
  },
  mounted() {
    this.newNickname = this.getNickname;
    this.height = this.getMyProfileInfo.height;
    this.size = this.getMyProfileInfo.size;
    console.log(
      "profile setting mounted",
      this.getNickname,
      this.getMyProfileInfo
    );
  },
  methods: {
    ...mapActions([
      "uploadProfile",
      "getProfileImage",
      "updateMyProfileInfoInApi",
      "getProfileInfoInApi",
      "logout"
    ]),
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
    updateProfile(newNickname, height, size) {
      // 프로필사진 업로드, 정보수정
      console.log('테스트1')
      this.dialog = false;
      this.uploadProfile()
      console.log('테스트2')

        .then(() => {
          this.getProfileImage({
            uid: this.getUid,
            target: "my",
          });
          this.setTargetProfileImage(this.getMyProfileImage);
        })
        .then(() => {
          this.clearUploads();
        });
      console.log('테스트3')

      this.updateMyProfileInfoInApi({
        newNickname,
        height,
        size,
      }).then(() => {
      console.log('테스트4')

        this.getProfileInfoInApi(this.newNickname);
        this.$router.push({ name: "Home" }).catch(() => {});
      });
    },
    nicknameCheck(nickname) {
      return authApi.post(`/v1/auth/nickname?nickname=${nickname}`)
      .then((res) => {
        console.log(res.data, '트루니?')
        if (res.data) {
          alert('사용가능한 닉네임입니다.')
          this.nicknameStatus = "yes"
        } else {
          alert('이미 사용중인 닉네임입니다.')
          this.nicknameStatus = "nope"

        }
      })
      },
    deleteUser() {
      return rscApi.put('/user/mypage/delete')
      .then(() => {
        console.log('회원탈퇴성공')
        this.logout()
        this.$router.push({name: 'Home'}).catch(() => {})
      })
    }
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