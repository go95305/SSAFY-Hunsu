<template>
  <v-layout justify-center>
    <v-carousel id="detailcard" v-model="model" height="350px">
      <v-carousel-item
        v-for="(color, i) in colors"
        :key="color"
      >
        <v-sheet
          :color="color"
          height="100%"
          tile
        >
          <v-row
            class="fill-height"
            align="center"
            justify="center"
          >
            <div class="display-3">
              Slide {{ i + 1 }}
            </div>
          </v-row>
        </v-sheet>
      </v-carousel-item>
    </v-carousel>
  </v-layout>
</template>

<script>
export default {
  name: "WhatWearDetailCard",
  data() {
    return {
      model: 0,
      colors: [
        'primary',
        'secondary',
        'yellow darken-2',
        'red',
        'orange',
      ]
    }
  }
}
</script>

<style>
#detailcard {
  width: 380px;
}
</style>