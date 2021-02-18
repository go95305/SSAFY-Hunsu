<template>
  <div>
    <LoginFilter />
    <LiveSearchBtn />
    <LiveWritePage style="margin: 10px" />
    <LiveList style="margin: 20px" />
  </div>
</template>

<script>
import LiveList from "@/components/live/LiveList";
import LiveWritePage from "@/components/live/LiveWritePage";
import LiveSearchBtn from "@/components/live/LiveSearchBtn";
import LoginFilter from "@/components/module/LoginFilter";

export default {
  name: "Live",
  components: {
    LiveList,
    LiveWritePage,
    LiveSearchBtn,
    LoginFilter,
  },
};
</script>

<style>
</style>