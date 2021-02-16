<template>
  <!-- 네비게이션 바 스크롤 v-app-bar에 app 코드 추가 -->
  <div>
    <v-app-bar color="white" dense height="50" app>
      <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> 메뉴아이콘-->
      <!--로고이미지-->
      <v-toolbar-title @click="goToHome()">
        <v-img src="@/assets/hunsulogo.png" width="80" id="logo"> </v-img>
      </v-toolbar-title>

      <v-spacer></v-spacer>
      <!-- <v-btn @click="getName()">닉네임출력</v-btn> -->
      <v-spacer></v-spacer>
      <!--로그인아이콘, 알림아이콘-->
      <div v-if="!getNickname">
        <v-icon class="mr-2" color="black" @click="goToLogin()"
          >mdi-account-outline</v-icon
        >
      </div>
      <!-- <div>
        <v-btn @click="goToLogin()">로그인</v-btn>
      </div> -->
      <!--로그인 후 알림아이콘 옆에 표시할 프로필사진 + mypage, logout menu바-->
          <div class="mt-1" v-if="getNickname">{{getNickname}}님 </div>
      <v-menu left bottom v-if="getNickname">
        <template v-slot:activator="{ on, attrs }">
          <v-avatar v-bind="attrs" v-on="on">
            <v-img v-if="getMyProfileImage" :src="getMyProfileImage" 
              class="mt-1"
              max-width="40px"
              height="40px"
              style="border-radius: 20px"/>
            <v-img v-else src="@/assets/profilephoto.png" 
              class="mt-1"
              max-width="40px"
              height="40px"
              style="border-radius: 20px"
            />
          </v-avatar>
        </template>

        <v-list>
          <v-list-item
            v-for="(item, i) in items"
            :key="i"
            @click="goToPage(item)"
          >
            <v-list-item-title>{{ item.text }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>

    </v-app-bar>
  </div>
</template>

<script>
import { mapGetters, mapActions, mapMutations } from "vuex";

export default {
  name: "SubNavBar",
  computed: {
    ...mapGetters([
      "getNickname",
      "getAccessToken",
      "getRefreshToken",
      "getMyProfileImage",
      "getTargetProfileImage",
      "getUid",
      "getAccessToken",
    ]),
  },
  data() {
    return {
      items: [
        {
          text: "MyPage",
        },
        {
          text: "Logout",
        },
      ],
    };
  },

  methods: {
    ...mapActions([
      "kakaoLogin",
      "getProfileInfoInApi",
      "getMyProfileInfoInApi",
      "getImages",
      "getImageList",
      "getProfileImage",
      "logout",
    ]),
    ...mapMutations(["setMyProfileImage", "setAllInfoClear"]),
    async goToPage(item) {
      // console.log(item.text)
      if (item.text === "MyPage") {
        // 여기선 자기 자신의 마이페이지로 이동
        // 타겟 유저의 프로필 정보 가져오기
        console.log('내닉네임', this.getNickname)
        await this.getProfileInfoInApi(this.getNickname);
        // await this.getMyProfileInfoInApi();
        await this.getProfileImage({
          // 타겟 유저의 이미지 정보 가져오기
          uid: this.getUid,
          target: "target",
        });
        this.$router.push({ name: "MyPage" }).catch(() => {});
      } else if (item.text === "Logout") {
        this.logout();
      }
    },
    getName() {
      // 변수 할당 확인 버튼 ( test )
      console.log(
        "image ",
        this.getMyProfileImage,
        "nickname",
        this.getNickname,
        "tokens",
        this.getAccessToken,
        "refresh",
        this.getRefreshToken,
        "uid",
        this.getUid
      );
    },
    goToHome() {
      this.$router.push("/").catch(() => {})
    },
    goToLogin() {
      this.$router.push({name: 'Login'}).catch(() => {})
    },

  },
};
</script>

<style>
#logo {
  margin: 0 auto;
}
</style>