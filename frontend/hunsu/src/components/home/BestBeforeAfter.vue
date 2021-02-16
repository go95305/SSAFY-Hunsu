<template>
<!-- 베스트 전후 -->
  <v-card
    elevation="24"
    class="mx-auto mt-3"
    flat
  >
    <v-card-title class="font-weight-regular ml-13" style="font-size: 1.25rem">Best Solution Of This Week </v-card-title>
    <v-system-bar lights-out class="d-flex justify-space-around">
      <div style="font-size: 1.25rem">BEFORE</div>
      <div style="font-size: 1.25rem">AFTER</div>  
      </v-system-bar>
    <v-carousel
      class="mt-3"
      :continuous="false"
      :cycle="cycle"
      :show-arrows="false"
      hide-delimiter-background
      delimiter-icon="mdi-minus"
      height="399px"
    >
      <v-carousel-item
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
              <img :src="slide" alt="" width="399px">
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
        cycle: true,
        slides: [
          'https://postfiles.pstatic.net/MjAyMTAyMTZfNjMg/MDAxNjEzNDQzNTQwOTQ4.OaxXUeuIRgKEdz8tZuhIzmBowSDYqiow03MpA5cwCdEg.o9LJ_3KvXGZSgc8lVbHZVZPsd-frHuKM6KYy0wvq4xIg.JPEG.hidy2151/111.jpg?type=w580',
          'https://postfiles.pstatic.net/MjAyMTAyMTZfOSAg/MDAxNjEzNDQzNTQ0MjM1.EU5RSwImw6YrCQ4kZUyw6CUAsEjuXbWltYKkPYgXwDog.VHUZMUm9kepeYPBDqQFPZaR12eq40vzswFY4YL6Q98Mg.JPEG.hidy2151/222.jpg?type=w580',
        ],
      }
  }
}
</script>

<style>

</style>