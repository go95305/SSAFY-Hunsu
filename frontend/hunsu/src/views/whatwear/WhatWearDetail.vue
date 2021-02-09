<template>
  <v-card>
    <div id="whatwear_profile">
      <v-list one-line>
        <v-list-item>
          <v-list-item-content class="pb-0">
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

          <!--작성자nickname과 로그인nickname이 같을때만 삭제버튼 출력-->
          <v-menu bottom v-if="getWhatwearInfo.nickname === getNickname">
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
      <!-- <div id="vote_input">
        <v-checkbox
          v-for="(n, index) in getWhatwearVoteInfo"
          :key="n.idx"
          :label="`${index+1}번`"
          v-model="n.choice"
          @click="voteWhatwear(getWhatwearVoteInfo[index].idx, getNickname)"
        ></v-checkbox>
      </div> -->
      <v-row justify="center" class="mb-5">
    <v-dialog
      v-model="dialog"
      scrollable
      max-width="300px"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          color="black"
          dark
          v-bind="attrs"
          v-on="on"
          text
        >
          Choice✨
        </v-btn>
      </template>
      <v-card>
        <v-card-title>투표창</v-card-title>
        <v-divider></v-divider>
        <v-card-text style="height: 300px;">
          <v-card-subtitle class="pa-0 mt-4">중복투표가능</v-card-subtitle>
          <v-checkbox
            v-for="(n, index) in getWhatwearVoteInfo"
            :key="n.idx"
            :label="`${index+1}번`"
            v-model="n.choice"
            @click="voteWhatwear(getWhatwearVoteInfo[index].idx, getNickname)"
          ></v-checkbox>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-btn
            color="gray"
            text
            @click="dialog = false"
          >
            취소
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            @click="dialog = false"
          >
            확인
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
      <!--투표결과그래프-->
      <div v-if="getVoteTotal">
        <div 
          id="vote_linear"
          v-for="(value, idx) in getWhatwearVoteInfo"
          :key="idx">
          <v-progress-linear
            color="light-blue"
            height="10"
            :value="(value.count / getVoteTotal ) * 100"
            striped
          ></v-progress-linear>
          <br>
        </div>
      </div>

      <div v-if="getVoteTotal === 0">
        <div 
          id="vote_linear"
          v-for="(value, idx) in getWhatwearVoteInfo"
          :key="idx">
          <v-progress-linear
            color="light-blue"
            height="10"
            value="0"
            striped
          ></v-progress-linear>
          <br>
        </div>
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
    ...mapGetters(["getWhatwearInfo", "getWhatwearVoteInfo", "getNickname", "getVoteTotal"])
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
      vote_activated: false,
    }
  },
  methods: {
    ...mapMutations(["setWhatwearInfo"]),
    ...mapActions(["getWhatwearInfoApi", "voteWhatwearInfo"]),
    // 글 삭제 함수
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
    // 투표하는 함수
    voteWhatwear(voteIdx, nickname) {
      this.voteWhatwearInfo({voteIdx, nickname})
    },
  }
}

</script>

<style>
#vote_input {
  margin-left: 18px;
  text-align: center;
}
#vote_linear {
  margin-left: 40px;
  margin-right: 40px;
}
</style>