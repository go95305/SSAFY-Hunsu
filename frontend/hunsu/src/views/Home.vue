<template>
  <div>
    <Bestootd />
    <AboutUsCard />
    <BestBeforeAfter />
    <!-- <RecommendBtn /> -->
  </div>
</template>

<script>
import Bestootd from "@/components/home/Bestootd";
import AboutUsCard from "@/components/home/AboutUsCard";
import BestBeforeAfter from "@/components/home/BestBeforeAfter";
// import RecommendBtn from "@/components/home/RecommendBtn";

export default {
  name: "Home",
  components: {
    Bestootd,
    BestBeforeAfter,
    // RecommendBtn,
    AboutUsCard,
  },
};
</script>

<style>
</style>