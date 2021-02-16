<template>
  <v-app id="app">
    <div>
      <MainNavBar />
      <v-app id="nav_content">
        <!--margin-top 걸려있음(네브바가 스크롤기능이 되면서 화면이 위로 올라가는걸 방지하기위해)-->
        <router-view />
      </v-app>
      <!-- <MainBottomBar /> bottom navigationbar -->
      <MainFooter />
    </div>
      <!-- <v-app> -->
        <!--margin-top 걸려있음(네브바가 스크롤기능이 되면서 화면이 위로 올라가는걸 방지하기위해)-->
        <!-- <router-view /> -->
      <!-- </v-app> -->
  </v-app>
</template>

<script>
import MainFooter from "@/components/layout/MainFooter";
import MainNavBar from "@/components/layout/MainNavBar";
// import MainBottomBar from '@/components/layout/MainBottomBar'
import { mapGetters, mapActions } from "vuex";
export default {
  name: "App",
  components: {
    MainFooter,
    MainNavBar,
    // MainBottomBar
  },
  computed: {
    ...mapGetters([
      "getNickname",
      "getAccessToken",
      "getRefreshToken",
      "getUid",
    ]),
    nickname() {
      return this.getNickname;
    },
  },
  watch: {
    nickname(val) {
      if (!this.typeCheck(val)) {
        this.$router.push("/login");
      }
    },
  },
  // created() {
  //   if (this.$route.name === 'Login') {
  //     this.show = false
  //   }
  //   if (this.$route.name === 'SignUp') {
  //     this.show = false
  //   }
  //   console.log(this.show)
  //   },
  async mounted() {
    // 자동로그인 처리
    if (!this.typeCheck(this.getAccessToken)) {
      // this.$router.push("/login");
    } else if (
      this.typeCheck(this.getNickname) &&
      this.typeCheck(this.getAccessToken)
    ) {
      await this.kakaoLogin();
      await this.getProfileImage({
        uid: this.getUid,
        target: "my",
      });
    }
  },
  data() {
    return {
      // show: true,
    };
  },
  methods: {
    ...mapActions(["kakaoLogin", "getProfileImage", "getImageList"]),
    typeCheck(str) {
      // console.log("typeCheck", typeof str);
      if (
        typeof str === "undefined" ||
        str == null ||
        str == "" ||
        str === "undefined"
      ) {
        return false;
      }
      return true;
    },
  },
};
</script>

<style>
#nav_content {
  background-color: #f5f5f5;
  margin-top: 100px;
}
</style>