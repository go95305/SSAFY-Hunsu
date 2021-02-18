<template>
  <v-app id="app">
    <SubNavBar v-if="$route.name == 'SignUp' || $route.name == 'Login'" />
    <MainNavBar v-else />
    <div v-if="$route.name == 'SignUp' || $route.name == 'Login'">
      <v-app id="userContent">
        <router-view />
      </v-app>
    </div>
    <div v-else>
      <v-app id="content">
        <router-view />
      </v-app>
    </div>
    <SubNavBar v-if="$route.name == 'SignUp' || $route.name == 'Login'" />
    <MainFooter v-else />
  </v-app>
</template>

<script>
import MainFooter from "@/components/layout/MainFooter";
import MainNavBar from "@/components/layout/MainNavBar";
import SubNavBar from "@/components/layout/SubNavBar";
import { mapGetters, mapActions } from "vuex";
export default {
  name: "App",
  components: {
    MainFooter,
    MainNavBar,
    SubNavBar,
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

  methods: {
    ...mapActions(["kakaoLogin", "getProfileImage", "getImageList"]),
    typeCheck(str) {
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