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
        <div v-if="first">
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
              @click="goToNext()"
              disabled
            >
              다음
            </v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <!--진행 상태 선, 작성이 다 완료 되었을때 value50으로-->
        <v-progress-linear 
          v-model="valueDeterminate"
          color="red accent-3">
        </v-progress-linear>
          <!--1단계 작성폼-->
          <v-list
            three-line
            subheader
          >
            <v-list-item>
              <v-list-item-content>
                <v-list-item-title class="text-h6 font-weight-bold mb-2">사진업로드</v-list-item-title>
                  <v-img
                    max-height="300"
                    max-width="400"
                    src="@/assets/null_photo.png"
                  ></v-img>
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
                <v-img
                  max-height="300"
                  max-width="400"
                  src="@/assets/null_photo.png"
                  ></v-img>
                <v-file-input
                  :disabled="!vote"
                  multiple
                  accept="image/*"
                  label="File input"
                ></v-file-input>
            </v-list-item-content>
          </v-list>
        </div>

        <!--다음 버튼 눌렀을 때 보여 줄 공간-->
        <div v-else>
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
              @click="CreateWhatWear()"
            >
              완료
            </v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <v-progress-linear v-model="valueDeterminate"></v-progress-linear>
          <v-list
            three-line
            subheader
          >
            <v-list-item>
              <v-list-item-content>
                <v-list-item-title>두번째</v-list-item-title>
                  <v-file-input
                    accept="image/*"
                    label="File input"
                  ></v-file-input>
              </v-list-item-content>
            </v-list-item>
            <v-list-item>
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
          <v-list
            three-line
            subheader
          >
            <v-list-item>
              <v-list-item-action>
                <v-checkbox v-model="notifications"></v-checkbox>
              </v-list-item-action>
              <v-list-item-content>
                <v-list-item-title class="mt-10">투표기능</v-list-item-title>
                <v-list-item-subtitle></v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
            <v-list-item-content>
              <v-list-item-title>사진업로드</v-list-item-title>
                <v-file-input
                  multiple
                  accept="image/*"
                  label="File input"
                ></v-file-input>
            </v-list-item-content>
          </v-list>
        </div>
      </v-card>
    </v-dialog>
  </v-layout>
</template>

<script>
export default {
  name: "WhatWearWrite",
  data() {
    return {
      dialog: false,
      notifications: false,
      valueDeterminate: 0,
      first: true,
      vote: false,
    }
  },
  methods: {
    goToNext() {
      // 첫번째 단계에서 두번째 단계 창 이동 + 진행바 절반채우기
      // 진행바 클릭하면 반응하는거 막아야함
      // 입력값이 채워졌는지 확인이랑 안채워졌을때 '값을 입력하라는' 알림창기능추가하기
      this.first = false
      this.valueDeterminate = 50
    },
    CreateWhatWear() {
      // dialog창 닫기 + 입력데이터 보내기
      this.dialog = false
    }
  }
}
</script>

<style>
#whatwear_input_vote {
  display: -webkit-box;
}

</style>