<template>
  <!-- 오오티디, 뭘입을까 리스트, 디테일에서 보이는 이미지 뷰 -->
  <!-- <v-carousel :show-arrows="false" v-if="!images"> -->
  <!-- <p>loading..</p> -->
  <!-- </v-carousel> -->
  <v-carousel
    v-if="images"
    :continuous="false"
    :cycle="true"
    :show-arrows="true"
    hide-delimiter-background
    delimiter-icon="mdi-minus"
    height="330"
  >
    <v-carousel-item v-for="(imageUrl, i) in images" :key="i">
      <v-sheet height="100%" tile>
        <v-row class="fill-height" align="center" justify="center">
          <v-img :src="imageUrl" />
        </v-row>
      </v-sheet>
    </v-carousel-item>
  </v-carousel>
  <v-carousel v-else>
    <v-progress-circular indeterminate color="primary"></v-progress-circular>
  </v-carousel>
</template>

<script>
export default {
  data() {
    return {};
  },
  mounted() {
    console.log("inImageView", this.images)
  },
  props: {
    images: {
      type: Array,
    },
  },
};
</script>

<style>
</style>