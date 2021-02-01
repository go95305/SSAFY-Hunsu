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
            <v-list-item>
              <v-list-item-content>
                <v-text-field
                  v-model="whatwear_title"
                  label="제목"
                  clearable
                ></v-text-field>
                <v-textarea
                  v-model="whatwear_content"
                  clearable
                  clear-icon="mdi-close-circle"
                  label="내용"
                ></v-textarea>
              </v-list-item-content>
            </v-list-item>
          </v-list>
          <v-list
            three-line
            subheader
          >
            <v-list-item>

              <v-list-item-content>
                  <v-list-item-title class="text-h6 font-weight-bold">사진업로드</v-list-item-title>
                  <v-img
                    v-if="image" :src="image_url" id="test" contain>
                  </v-img>
                  <v-file-input
                    accept="image/*"
                    label="File input"
                    chips
                    clearable
                    v-model="image"
                    @change="previewImage()"
                  ></v-file-input>

                  <div id="votebtn">
                  <v-checkbox v-model="vote"></v-checkbox>
                  <v-list-item-title class="text-h6 font-weight-bold">투표기능</v-list-item-title>
                  </div>
                  <v-img 
                    v-for="(vote_image_url, idx) in vote_image_urls" 
                    :key="idx" 
                    :src="vote_image_url">
                  </v-img>
                  <v-file-input
                    :disabled="!vote"
                    multiple
                    chips
                    accept="image/*"
                    label="File input"
                    v-model="vote_image"
                    @change="previewVoteImage()"
                  ></v-file-input>

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
      whatwear_title: '',
      whatwear_content: '',
      vote: false,
      image: null,
      image_url: null,
      vote_image: null,
      vote_image_urls: [],
    }
  },
  methods: {
    createWhatWear() {
      // dialog창 닫기 + 입력데이터 보내기
      this.dialog = false
      const params = {
        'content': this.whatwear_content,
        'nickname': 'wogml23',
        'num': 0,
        'title': this.whatwear_title,
        
      }
      // 작성폼 초기화
      this.whatwear_title = '',
      this.whatwear_content = '',
      axios.post('http://i4c102.p.ssafy.io:8080/api/wear', params)
        .then(() => {
          console.log('뭘입을까글쓰기성공')
        })
        .catch(err => {
          console.error(err)
        })
    },
    previewImage() {
      // console.log(this.image)
      // this.image_url = null
      // if (this.image_url !== null) {
      //   this.image_url = null
      // }
      this.image_url = URL.createObjectURL(this.image)
    },
    previewVoteImage() {
      console.log(this.vote_image)
      this.vote_image.forEach(e => this.vote_image_urls.push(URL.createObjectURL(e)))
      console.log(this.vote_image_urls)
    }
  }
}
</script>

<style>
#votebtn {
  display: flex;
}

</style>