<template>
  <div hidden />
</template>
<script>
import { mapGetters } from "vuex";
export default {
  computed: {
    ...mapGetters(["getAccessToken"]),
  },
  created() {
    if (this.getAccessToken === null) {
      alert("먼저 로그인을 해주세요!");
      this.$router.push("/login");
    }
  },
};
</script>

<style>
</style>