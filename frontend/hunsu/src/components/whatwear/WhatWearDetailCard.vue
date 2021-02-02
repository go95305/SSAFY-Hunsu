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
      <v-menu bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-avatar v-bind="attrs" v-on="on">
            <v-btn color="black" icon class="d-inline-block">
              <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>
          </v-avatar>
        </template>
        <v-list>
          <v-dialog
            v-model="dialog"
            persistent
            max-width="290"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                color="primary"
                dark
                v-bind="attrs"
                v-on="on"
                text
              >
              삭제
              </v-btn>
            </template>
            <v-card>
              <v-card-title class="headline">
                삭제하기
              </v-card-title>
              <v-card-text>게시글을 삭제하시겠어요?</v-card-text>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn
                    color="blue darken-4"
                    text
                    @click="dialog = false"
                  >
                    취소
                  </v-btn>
                  <v-btn
                    color="red accent-4"
                    text
                    @click="[dialog = false, deleteWhatWear()]"
                  >
                    삭제하기
                  </v-btn>
                </v-card-actions>
            </v-card>
          </v-dialog>
        </v-list>
      </v-menu>
      
    
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
      dialog: false,
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
    console.log(this.nickname)
  },
  methods: {
    getWhatwearDetail() {
      const wearIdx = this.wear_idx
      const nickname = this.nickname
      axios.get(`http://i4c102.p.ssafy.io:8080/api/wear/detail/${wearIdx}/${nickname}`)
        .then(res => {
          // console.log(res)  
          this.whatwearInfo = res.data
          this.writeDate = this.whatwearInfo.write_date.slice(0, 10)
          this.writeTime = this.whatwearInfo.write_date.slice(12, 16)
        })
        .catch(err => {
          console.error(err)
        })
    },
    deleteWhatWear() {
      const wearIdx = this.wear_idx
      axios.put(`http://i4c102.p.ssafy.io:8080/api/wear/${wearIdx}`)
        .then(res => {
          console.log(res)
          this.$router.push({name: 'WhatWear'})
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