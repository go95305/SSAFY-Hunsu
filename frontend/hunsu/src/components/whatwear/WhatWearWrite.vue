<template>
  <v-layout justify-end>
    <v-dialog
      v-model="dialog"
      fullscreen
      hide-overlay
      transition="dialog-bottom-transition"
    >
      <!--뭘입을까 메인화면에 보이는 작성버튼-->
      <template v-slot:activator="{ on, attrs }">
        <div id="whatwear_writebtn">
          <v-btn
            color="red accent-3"
            dark
            small
            fab
            v-bind="attrs"
            v-on="on"
          >
            <v-icon>mdi-pencil</v-icon>
          </v-btn>
        </div>
      </template>
      
      <v-card>
        <!--작성창 상단바-->
        <v-toolbar
          dark
          color="black"
        >
          <v-btn
            icon
            dark
            @click="dialog = false"
          >
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>뭘입을까 작성</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <v-btn
              
              text
              @click="createWhatWear()"
            >
              완료
            </v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <v-list
            three-line
            subheader
          >
          <!--글작성 폼 시작 -->
          <!--제목, 내용-->
            <v-list-item>
              <v-list-item-content>
                <v-text-field
                  v-model="whatwearTitle"
                  label="제목"
                  clearable
                  counter="30"
                  :rules="[rules.required, rules.min, rules.titleMax]"
                ></v-text-field>
                <v-textarea
                  v-model="whatwearContent"
                  clearable
                  clear-icon="mdi-close-circle"
                  :rules="[rules.required, rules.min, rules.contentMax]"
                  counter="300"
                  label="내용"
                ></v-textarea>
              </v-list-item-content>
            </v-list-item>
          </v-list>
          <!--사진업로드-->
          <v-list
            three-line
            subheader
          >
          <v-list-item>
            <v-list-item-content>
              <v-list-item-title class="text-h6 font-weight-bold">사진업로드</v-list-item-title>
              <v-img
                v-if="image" :src="imageUrl" id="test" contain>
              </v-img>
              <v-file-input
                accept="image/*"
                label="File input"
                chips
                clearable
                v-model="image"
                @change="previewImage()"
              ></v-file-input>
              <!--투표기능-->
              <div id="votebtn">
              <v-checkbox v-model="vote"></v-checkbox>
              <v-list-item-title class="text-h6 font-weight-bold">투표기능</v-list-item-title>
              </div>
              <v-img 
                v-for="(voteImageUrl, idx) in voteImageUrls" 
                :key="idx" 
                :src="voteImageUrl">
              </v-img>
              <v-file-input
                :disabled="!vote"
                multiple
                chips
                accept="image/*"
                label="File input"
                v-model="voteImage"
                @change="previewVoteImage()"
              ></v-file-input>   
                     <v-dialog
                      ref="dateDialog"
                      v-model="dateDialog"
                      :return-value.sync="dates"
                      persistent
                      width="290px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="dates"
                          label="Picker in dialog"
                          prepend-icon="mdi-clock-time-four-outline"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                          :disabled="!vote"
                        ></v-text-field>
                      </template>
                      <v-date-picker
                        v-model="dates"
                        full-width
                        color="black"
                      >
                        <v-spacer></v-spacer>
                        <v-btn
                          text
                          color="black"
                          @click="dateDialog = false"
                        >
                          Cancel
                        </v-btn>
                        <v-btn
                          text
                          color="black"
                          @click="[$refs.dateDialog.save(dates), inputDate(dates)]"
                        >
                          OK
                        </v-btn>
                      </v-date-picker>
                    </v-dialog>
                    <v-dialog
                      ref="timeDialog"
                      v-model="timeDialog"
                      :return-value.sync="time"
                      persistent
                      width="290px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="time"
                          label="Picker in dialog"
                          prepend-icon="mdi-clock-time-four-outline"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                          :disabled="!vote"
                        ></v-text-field>
                      </template>
                      <v-time-picker
                        v-if="timeDialog"
                        v-model="time"
                        full-width
                        color="black"
                      >
                        <v-spacer></v-spacer>
                        <v-btn
                          text
                          color="black"
                          @click="timeDialog = false"
                        >
                          Cancel
                        </v-btn>
                        <v-btn
                          text
                          color="black"
                          @click="[$refs.timeDialog.save(time), inputTime(time)]"
                        >
                          OK
                        </v-btn>
                      </v-time-picker>
                    </v-dialog>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-card>
    </v-dialog>
  </v-layout>
</template>

<script>
import axios from "axios";

export default {
  name: "WhatWearWrite",
  data() {
    return {
      dialog: false,
      notifications: false,
      whatwearTitle: '',
      whatwearContent: '',
      vote: false,
      image: null,
      imageUrl: null,
      voteImage: null,
      voteImageUrls: [],
      rules: {
        required: v => !!v || "Required",
        min: v => v.trim().length > 0 || "공백안됨",
        titleMax: v => v.length <= 30 || "30자이하",
        contentMax: v => v.length <= 300 || "300자이하",
      },
      time: '',
      menu2: false,
      timeDialog: false,
      dateDialog: false,
      dates: '',
    }
  },
  methods: {
    createWhatWear() {
      // dialog창 닫기 + 입력데이터 보내기
      this.dialog = false
      const endtime = this.dates.concat('T0', this.time, ':00.469Z')
      console.log(endtime)
      const params = {
        'content': this.whatwearContent,
        'endtime': endtime,
        'nickname': 'wogml23',
        'num': 1,
        'title': this.whatwearTitle,
        
      }
      // 작성폼 초기화
      this.whatwearTitle = '',
      this.whatwearContent = '',
      axios.post('http://i4c102.p.ssafy.io:8080/api/wear', params)
        .then(() => {
          console.log('뭘입을까글쓰기성공')
          console.log(params)
        })
        .catch(err => {
          console.error(err)
        })
    },
    previewImage() {
      this.imageUrl = URL.createObjectURL(this.image)
    },
    previewVoteImage() {
      console.log(this.voteImage)
      this.voteImage.forEach(e => this.voteImageUrls.push(URL.createObjectURL(e)))
      console.log(this.voteImageUrls)
    },
    inputDate(dates) {
      this.dates = dates
    },
    inputTime(time) {
      this.time = time
    }
  }
}
</script>

<style>
#votebtn {
  display: flex;
}

</style>