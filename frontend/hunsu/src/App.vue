<template>
  <v-app id="app">
    <SubNavBar v-if="($route.name == 'SignUp') || ($route.name == 'Login')"/>
    <MainNavBar v-else/>
    <div v-if="($route.name == 'SignUp') || ($route.name == 'Login')">
    <v-app  id="userContent">
      <router-view />
    </v-app>
    </div>
    <div v-else>
      <v-app id="content" >
        <router-view />
      </v-app>
    </div>
    <!-- <MainBottomBar /> bottom navigationbar -->
    <SubNavBar v-if="($route.name == 'SignUp') || ($route.name == 'Login')"/>
    <MainFooter v-else/>
  </v-app>
</template>

<script>
import MainFooter from "@/components/layout/MainFooter";
import MainNavBar from "@/components/layout/MainNavBar";
import SubNavBar from "@/components/layout/SubNavBar";
// import MainBottomBar from '@/components/layout/MainBottomBar'
import { mapGetters, mapActions } from "vuex";
export default {
  name: "App",
  components: {
    MainFooter,
    MainNavBar,
    SubNavBar,
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
#content {
  background-color: whitesmoke;
  margin-top: 100px;
}
#userContent {
  background-color: whitesmoke;
  margin-top: 50px;
}
</style>