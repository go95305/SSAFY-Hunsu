<template>
  <v-card
    max-width="400"
  >
    <v-img
      src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg"
      height="200px"
      @click="goToWhatWearDetail()"
    >    
    </v-img>
    <v-card-title>
      제목
    </v-card-title>
    <v-card-subtitle>
      내용
    </v-card-subtitle>
  </v-card>
</template>

<script>
export default {
  name: "WhatWearList",
  methods: {
    goToWhatWearDetail() {
      this.$router.push('/whatwear/detail')
    }
  }
}
</script>

<style>

</style>