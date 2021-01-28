<template>
  <div id="vote">
    <!--투표창-->
    <p style="margin-left: 20px" class="text-body-1 font-weight-black">투표결과</p>
    <v-radio-group v-model="radioGroup" id="vote_input">
      <v-radio
        v-for="n in 3"
        :key="n"
        :label="`Radio ${n}`"
        :value="n"
      ></v-radio>
    </v-radio-group>
    <!--투표결과그래프-->
    <div id="vote_chart">
      <chartjs-doughnut
        :labels="labels"
        :datasets="datasets"
        :option="option"
      ></chartjs-doughnut>
    </div>
  </div>

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
  width: 370px; 
}

#vote_input {
  margin-left: 18px;
  text-align: center;
}

/* #vote_chart { 
  margin-left: 5px;
} */
</style>