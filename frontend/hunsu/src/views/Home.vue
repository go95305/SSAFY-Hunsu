<template>
  <div>
    <Bestootd />
    <AboutUsCard />
    <BestBeforeAfter />
  </div>
</template>

<script>
import Bestootd from "@/components/home/Bestootd";
import AboutUsCard from "@/components/home/AboutUsCard";
import BestBeforeAfter from "@/components/home/BestBeforeAfter";

export default {
  name: "Home",
  components: {
    Bestootd,
    BestBeforeAfter,
    AboutUsCard,
  },
};
</script>

<style>
</style>