<template>
  <v-layout justify-center>
    <div id="vote">
      <!--투표창-->
      <v-radio-group v-model="radioGroup">
        <v-radio
          v-for="n in 3"
          :key="n"
          :label="`Radio ${n}`"
          :value="n"
        ></v-radio>
      </v-radio-group>
      <!--투표결과그래프-->
      <v-progress-linear
        color="light-blue"
        height="10"
        value="10"
        striped
      ></v-progress-linear>
      <br>
      <v-progress-linear
        color="light-green darken-4"
        height="10"
        value="20"
        striped
      ></v-progress-linear>
      <br>
      <v-progress-linear
        height="10"
        value="45"
        striped
        color="lime"
      ></v-progress-linear>
      <br>
      <v-progress-linear
        value="60"
        height="10"
        striped
        color="deep-orange"
      ></v-progress-linear>
    </div>
  </v-layout>
</template>

<script>
export default {
  name: "WhatWearChart",
  data() {
    return {
      radioGroup: '',
    }
  }
}
</script>

<style>
#vote {
  width: 370px
}
</style>