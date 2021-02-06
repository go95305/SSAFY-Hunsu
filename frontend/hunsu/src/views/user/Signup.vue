<template>
  <div>hi</div>
</template>

<script>
export default {
  name: "Signup",
  props: {
    accessToken: {
      type: String,
    },
    refreshToken: {
      type: String,
    },
  },
  mounted() {
    console.log("signup ", this.accessToken, this.refreshToken);
  },
};
</script>

<style>
</style>