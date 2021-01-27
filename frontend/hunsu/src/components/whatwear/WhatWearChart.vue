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
    <chartjs-doughnut
      :labels="labels"
      :datasets="datasets"
      :option="option"
    ></chartjs-doughnut>
    </div>
  </v-layout>
</template>

<script>
export default {
  name: "WhatWearChart",
  data() {
    return {
      radioGroup: '',
      labels: ["1", "2", "3"],
      datasets: [
        {
          data: [20, 30, 50],
          backgroundColor: ["Red", "Yellow", "Purple"]
        }
      ],
      option: {
        title: {
          display: true,
          position: "bottom",
          text: "test"
        }
      }
    }
  },
}

</script>

<style>
#vote {
  width: 370px
}
</style>