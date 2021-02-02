<template>
  <div id="vote" v-if="vote_activated">
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
import axios from "axios";

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
      },
      vote_activated: false
    }
  },
  props: {
    nickname: {
      type: String,
    },
    wear_idx: {
      type: Number
    }
  },
  created() {
    this.getWhatwearDetail()
  },
  methods: {
    getWhatwearDetail() {
      const wearIdx = this.wear_idx
      const nickname = this.nickname
      axios.get(`http://i4c102.p.ssafy.io:8080/api/wear/detail/${wearIdx}/${nickname}`)
        .then(res => {
          // console.log(res)  
          this.vote_activated = res.data.vote_activated
        })
        .catch(err => {
          console.error(err)
        })
    }
  }
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