<template>
  <v-card
    class="mx-auto mt-10"
    max-width="600px"
    @click="goToWhatWearDetail()"
  >
  <div class="d-flex">
    <v-avatar
  v-bind="attrs"
  v-on="on"
  class="mt-2 ml-4"
  >
    <img
      src="https://cdn.vuetifyjs.com/images/john.jpg"
      alt="John"
    >
  </v-avatar>
  <v-card-title>
    제목
  </v-card-title>

  </div>
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