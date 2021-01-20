<template>
  <div id="card-section">
  <v-sheet>전후베스트</v-sheet>
  <v-carousel cycle v-model="model" hide-delimiters width="100vw" height="35vw">
    <!--  cycle -->
    <v-carousel-item>
      <img src="@/assets/test.png" alt="poster1" id="img">
    </v-carousel-item>
    <v-carousel-item>
      <img src="@/assets/test2.png" alt="poster2" id="img">
    </v-carousel-item>
  </v-carousel>
</div>
</template>

<script>
export default {
  name: "BestBeforeAfter",
  data() {
      return {
        model: 0,
      }
  }
}
</script>

<style>
#img {
  height: 100%;
  width: auto;
  vertical-align: middle;
  box-shadow: 0px 0px 100px #000;
  background-position: -100% 10% !important;
}
</style>