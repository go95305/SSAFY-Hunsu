<template>
  <v-layout justify-end>
    <v-dialog
      v-model="dialog"
      fullscreen
      hide-overlay
      transition="dialog-bottom-transition"
    >
      <!--뭘입을까 메인화면에 보이는작성버튼-->
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
              dark
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
                <v-list-item-title class="text-h6 font-weight-bold mb-2">사진업로드</v-list-item-title>
                  <v-file-input
                    accept="image/*"
                    label="File input"
                    hide-input
                  ></v-file-input>
              </v-list-item-content>
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
          <v-list
            three-line
            subheader
          >
            <v-list-item>
              <v-list-item-action id="whatwear_input_vote">
                <v-checkbox v-model="vote"></v-checkbox>
                <v-list-item-title class="mt-1 ml-1">투표기능</v-list-item-title>
              </v-list-item-action>
            </v-list-item>
            <v-list-item-content>
              <v-list-item-title class="text-h6 font-weight-bold mb-2">사진업로드</v-list-item-title>
                <v-file-input
                  :disabled="!vote"
                  multiple
                  accept="image/*"
                  label="File input"
                ></v-file-input>
                <v-text-field
                  v-model="whatweartitle"
                  label="제목"
                  clearable
                ></v-text-field>
                <v-text-field
                  v-model="content"
                  label="내용"
                  clearable
                ></v-text-field>
            </v-list-item-content>

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
      whatweartitle: '',
      content: '',
      vote: false,
      // image_url: '',
      // vote_image_url: '',
    }
  },
  methods: {
    createWhatWear() {
      // dialog창 닫기 + 입력데이터 보내기
      
      this.dialog = false
      const params = {
        'content': this.content,
        'nickname': 'test',
        'num': 0,
        'title': this.whatweartitle,
      }
      axios.post('http://localhost:8080/wear', params)
        .then(() => {
          console.log('뭘입을까글쓰기성공')
        })
        .catch(err => {
          console.error(err)
        })
    }
  }
}
</script>

<style>
#whatwear_input_vote {
  display: -webkit-box;
}

</style>