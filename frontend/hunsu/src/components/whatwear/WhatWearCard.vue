<template>
  <v-card flat class="mb-2" >
    <div >
        <v-carousel 
          v-model="model" 
          id="ww_carousel" 
          cycle 
          show-arrows-on-hover 
          hide-delimiter-background>
          <v-carousel-item>
          <v-img src="@/assets/whatwear2.png">
          </v-img>
          </v-carousel-item>
          <v-carousel-item>
          <v-img src="@/assets/sample2.png">
          </v-img>
          </v-carousel-item>
              <v-row
                class="fill-height"
                align="center"
                justify="center"
              >
              </v-row>
        </v-carousel>
      </div>
  </v-card>
</template>

<script>
export default {
  name: "WhatWearCard",
  data() {
  return {
        items: [
          {
            src: "@/assets/hunsulogo.png",
          },
          {
            src: 'https://cdn.vuetifyjs.com/images/carousel/sky.jpg',
          },
        ],
      model: 0,
    }
  }
}
</script>

<style>
#ww_carousel {
  height: 190px !important;
}
</style>