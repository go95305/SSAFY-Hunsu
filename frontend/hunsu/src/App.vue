<template>
  <v-app id="app">
    <MainNavBar />
    <v-app id="content">
      <!--margin-top 걸려있음(네브바가 스크롤기능이 되면서 화면이 위로 올라가는걸 방지하기위해)-->
      <router-view />
    </v-app>
    <!-- <MainBottomBar /> bottom navigationbar -->
    <MainFooter />
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
    ...mapGetters(["getNickname", "getAccessToken", "getRefreshToken"]),
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
  mounted() {
    console.log(
      "mounted",
      this.getNickname,
      this.getAccessTOken,
      this.getRefreshToken
    );
    console.log(this.typeCheck(this.getAccessToken));
    console.log(this.typeCheck(this.getNickname));
    console.log(this.typeCheck(this.getRefreshToken));
    if (!this.typeCheck(this.getAccessToken)) {
      this.$router.push("/login");
    } else if (
      this.typeCheck(this.getNickname) &&
      this.typeCheck(this.getAccessToken)
    ) {
      this.kakaoLogin().then(() => {
        // console.log(this.getNickname);
        this.getProfileImage({
          nickname: this.getNickname,
          target: "my",
        });
      });
    }
  },
  data() {
    return {};
  },
  methods: {
    ...mapActions(["kakaoLogin", "getProfileImage"]),
    typeCheck(str) {
      console.log("typeCheck", typeof str);
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
  background-color: #f5f5f5;
  margin-top: 100px;
}
</style>