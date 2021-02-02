<template>
  <div>
    <!--프로필사진, 닉네임-->
    <div id="whatwear_profile">
      <v-avatar
      class="mt-5 ml-5"
      >
      <img
        src="https://cdn.vuetifyjs.com/images/john.jpg"
        alt="John"
      >
      </v-avatar>
      <p style="margin:35px 0px 0px 10px;" class="text-subtitle-2 font-weight-bold">{{ whatwearInfo.nickname }}</p>
      <p>{{ writeDate }}</p>
      <p>{{ writeTime }}</p>
    
    </div>
    <!--게시글 제목, 글 내용-->
    <div id="whatwear_title">
      <p style="margin-bottom: 0" class="text-body-1 font-weight-bold">{{ whatwearInfo.title }}</p>
      <p style="margin-bottom: 0" class="text-caption">{{ whatwearInfo.content }}</p>
    </div>
    <!--사진슬라이드-->
    <v-carousel 
      v-model="model"
      hide-delimiter-background>
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
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "WhatWearDetailCard",
  data() {
    return {
      model: 0,
      colors: [
        'primary',
        'secondary',
        'yellow darken-2',
      ],
      whatwearInfo: {},
      writeDate: '',
      writeTime: '',
    }
  },
  created() {
    this.getWhatwearDetail()
  },
  methods: {
    getWhatwearDetail() {
      const wearIdx = this.$route.params.no
      const nickname = this.$route.params.keyword
      axios.get(`http://i4c102.p.ssafy.io:8080/api/wear/detail/${wearIdx}/${nickname}`)
        .then(res => {
          console.log(res)  
          this.whatwearInfo = res.data
          this.writeDate = this.whatwearInfo.write_date.slice(0, 10)
          this.writeTime = this.whatwearInfo.write_date.slice(12, 16)
        })
        .catch(err => {
          console.error(err)
        })
    }
  }
}
</script>

<style>
#detailcard {
  width: 380px;
}

#whatwear_profile {
  display: flex;
}

#whatwear_title {
  margin-left: 76px;
  margin-bottom: 10px;
}
</style>