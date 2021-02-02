<template>
  <div>
    <div>
      <!--코디추천버튼-->
      <RecommendBtn />
    </div>
    <Bestootd />
    <BestBeforeAfter />
  </div>
</template>

<script>
import Bestootd from "@/components/home/Bestootd";
import BestBeforeAfter from "@/components/home/BestBeforeAfter";
import RecommendBtn from "@/components/home/RecommendBtn";

export default {
  name: "Home",
  components: {
    Bestootd,
    BestBeforeAfter,
    RecommendBtn,
  },
};
</script>

<style>
</style>