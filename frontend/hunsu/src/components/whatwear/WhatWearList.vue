<template>
  <v-card
    class="mx-auto mt-10"
    max-width="600px"
    height="65px"
    @click="goToWhatWearDetail()"
  >
    <div class="d-flex">
      <v-avatar
      class="mt-2 ml-4"
      >
      <img
        src="https://cdn.vuetifyjs.com/images/john.jpg"
        alt="John"
      >
    </v-avatar>
    <v-card-subtitle
      class="mt-2 font-weight-bold">
      겨울맞이 아우터 좀 골라주세요!!
    </v-card-subtitle> 
      <v-badge
        color="red accent-3"
        content="투표"
        inline
        class="mt-6">
      </v-badge>
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