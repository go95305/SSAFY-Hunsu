<template>
  <v-card>
    <div id="whatwear_profile">
      <v-list one-line>
        <v-list-item>
          <v-list-item-content>
            <v-list-item-title class="text-h6">
              {{ getWhatwearInfo.title }}
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
      <v-list one-line>
        <v-list-item>
          <!--작성자정보-->
          <v-list-item-avatar>
            <!--프로필이미지-->
            <v-img
              src="https://cdn.vuetifyjs.com/images/john.jpg"
              alt="John"
              width="100"
            ></v-img>
          </v-list-item-avatar>
          <v-list-item-content>
              <!--닉네임-->
              <v-list-item-title class="text-subtitle-1">
                {{ getWhatwearInfo.nickname }}
              </v-list-item-title>
              <!--작성시간-->
              <v-list-item-title class="text-caption">
                {{ getWhatwearInfo.write_date.slice(0, 10) }}
                {{ getWhatwearInfo.write_date.slice(12, 16) }}
              </v-list-item-title>
          </v-list-item-content>

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
                    color="red accent-4"
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
                        @click="[dialog = false, deleteWhatWear(getWhatwearInfo.wear_idx)]"
                      >
                        삭제하기
                      </v-btn>
                    </v-card-actions>
                </v-card>
              </v-dialog>
            </v-list>
          </v-menu>
        </v-list-item>
      </v-list>
    </div>
    <!--투표x 사진-->
    <div v-if="!getWhatwearInfo.vote_activated">

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
    <!--투표사진-->
    <div id="vote" v-if="getWhatwearInfo.vote_activated">
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

    <!--글 내용-->
    <v-list one-line>
        <v-list-item>
            <v-list-item-title class="text-body-2">
              {{ getWhatwearInfo.content }}
            </v-list-item-title>
        </v-list-item>
      </v-list>

      
    <!--투표창-->
    <div v-if="getWhatwearInfo.vote_activated">
      <v-radio-group v-model="radioGroup" id="vote_input">
        <v-radio
          v-for="n in getWhatwearInfo.voteList.length"
          :key="n"
          :label="`${n}번`"
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
    <WhatWearDetailComment />
  </v-card>
  
</template>


<script>

import WhatWearDetailComment from "@/components/whatwear/WhatWearDetailComment"
import { mapActions, mapGetters, mapMutations } from 'vuex'

import axios from "axios"

export default {
  name: "WhatWearDetail",
  components: {
    WhatWearDetailComment,
  },
  computed: {
    ...mapGetters(["getWhatwearInfo", "labels"])
  },
  data() {
    return {
      model: 0,
      colors: [
        'primary',
        'secondary',
        'yellow darken-2',
      ],
      dialog: false,
      radioGroup: '',
      option: {
        title: {
          display: true,
          position: "bottom",
          text: "test"
        }
      },
      vote_activated: false,
      datasets: [
        {
          data: [20, 20, 20, 20, 20],
          backgroundColor: ['Red', 'Yellow', 'Purple', 'Black', 'Pink'],
        },
      ],
    }
  },
  methods: {
    ...mapMutations(["setWhatwearInfo"]),
    ...mapActions(["getWhatwearInfoApi"]),
    deleteWhatWear(wear_idx) {
      const wearIdx = wear_idx
      axios.put(`http://i4c102.p.ssafy.io:8080/api/wear/${wearIdx}`)
        .then(res => {
          console.log(res)
          this.$router.push({name: 'WhatWear'})
        })
        .catch(err => {
          console.error(err)
        })
    },
  }
}

</script>

<style>
#vote_input {
  margin-left: 18px;
  text-align: center;
}
</style>