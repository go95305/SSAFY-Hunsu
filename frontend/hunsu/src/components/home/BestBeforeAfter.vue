<template>
<!-- 베스트 전후 -->
  <v-card
    elevation="24"
    class="mx-auto mt-3"
    flat
  >
    <v-card-title class="font-weight-regular blue-grey darken-4 white--text mb-1 pl-9" style="font-size: 1.25rem">✨Best Solution Of This Week✨ </v-card-title>
    <v-system-bar lights-out class="d-flex justify-space-around">
      <div style="font-size: 1.25rem">BEFORE</div>
      <div style="font-size: 1.25rem">AFTER</div>  
      </v-system-bar>
    <v-carousel
      class="mt-1"
      :continuous="false"
      :cycle="cycle"
      :show-arrows="false"
      hide-delimiter-background
      delimiter-icon="mdi-minus"
      height="375px"
    >
    <v-carousel-item>
      <img src="@/assets/man.jpg" alt="" width="375">
    </v-carousel-item>
    <v-carousel-item>
      <img src="@/assets/woman.jpg" alt="" width="375">
    </v-carousel-item>
      <!-- <v-carousel-item
        v-for="(slide, i) in slides"
        :key="i"
      >
        <v-sheet
          height="100%"
          tile
        >
          <v-row
            class="fill-height"
            align="center"
            justify="center"
          >
            <div class="display-3">
              <img src="slide" alt="" width="399px">
            </div>
          </v-row>
        </v-sheet>
      </v-carousel-item> -->
    </v-carousel>
    <v-list two-line>
    </v-list>
  </v-card>
</template>

<script>
export default {
  name: "BestBeforeAfter",
  data() {
      return {
        cycle: true,
        slides: [
          "@/assets/man.jpg",
          "@/assets/woman.jpg"
        ],
      }
  }
}
</script>

<style>

</style>