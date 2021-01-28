<template>
<!-- 베스트 전후 -->
  <v-card
    elevation="24"
    max-width="344"
    class="mx-auto"
  >
    <v-system-bar lights-out></v-system-bar>
    <v-carousel
      :continuous="false"
      :cycle="cycle"
      :show-arrows="false"
      hide-delimiter-background
      delimiter-icon="mdi-minus"
      height="300"
    >
      <v-carousel-item
        v-for="(slide, i) in slides"
        :key="i"
      >
        <v-sheet
          :color="colors[i]"
          height="100%"
          tile
        >
          <v-row
            class="fill-height"
            align="center"
            justify="center"
          >
            <div class="display-3">
              {{ slide }} Slide
            </div>
          </v-row>
        </v-sheet>
      </v-carousel-item>
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
        colors: [
          'green',
          'secondary',
        ],
        cycle: true,
        slides: [
          'First',
          'Second',
        ],
      }
  }
}
</script>

<style>

</style>