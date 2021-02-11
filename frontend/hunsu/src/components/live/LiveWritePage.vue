<template>
  <!-- 라이브 개설 페이지 (OPEN LIVE버튼 포함) -->
  <v-row justify="center" class="d-inline-block">
    <v-dialog
      v-model="dialog"
      fullscreen
      hide-overlay
      transition="dialog-bottom-transition"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn color="red accent-3" dark v-bind="attrs" v-on="on">
          Open Live
        </v-btn>
      </template>
      <v-card>
        <v-toolbar dark color="black">
          <v-btn icon dark @click="dialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>라이브 개설</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <v-btn dark text @click="create"> 개설하기 </v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <v-list three-line subheader>
          <v-subheader>라이브 설명</v-subheader>
          <div>
            <v-text-field
              v-model="title"
              label="제목 입력"
              :rules="rules"
              hide-details="auto"
              class="px-5"
            ></v-text-field>
            <!-- <v-text-field label="고정댓글 설정" class="px-5"></v-text-field> -->
          </div>
        </v-list>
        <v-divider></v-divider>
        <v-list three-line subheader>
          <v-text-field
            color="deep-purple accent-1"
            label="해시태그 추가"
            @keydown.enter="addHashtag()"
            v-model="hashtag"
            class="px-5"
          ></v-text-field>
          <div class="mx-4">
            <v-chip
              v-for="(hashtag, idx) in hashtagList"
              :key="idx"
              class="mx-1 my-1"
              close
              color="deep-purple accent-1"
              text-color="white"
              @click:close="deleteHashtag(hashtag)"
            >
              {{ hashtag }}
            </v-chip>
          </div>
        </v-list>
        <v-list three-line subheader>
          <v-subheader>사진 등록</v-subheader>
          <input ref="imageInput" type="file" hidden @change="onChangeImages" />
          <v-btn class="mx-5" type="button" @click="onClickImageUpload"
            >사진 업로드</v-btn
          >
          <v-img
            class="mx-5 my-5"
            v-if="imageUrl"
            :src="imageUrl"
            width="100"
          ></v-img>
        </v-list>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
// import axios from "axios";
import { mapActions } from "vuex";

export default {
  name: "LiveWritePage",
  data() {
    return {
      dialog: false,
      notifications: false,
      sound: true,
      widgets: false,
      rules: [
        (value) => !!value || "Required.",
        (value) => (value && value.length >= 3) || "Min 3 characters",
      ],
      imageUrl: null,
      title: "",
      hashtag: "",
      hashtagList: [],
    };
  },
  methods: {
    ...mapActions(["createRoom", "findAllRoom"]),
    async create() {
      await this.createRoom({
        title: this.title,
        hashtagList: this.hashtagList,
      }).then((res) => {
        if (res === 1) {
          this.dialog = false;
          this.title = "";
          this.hashtagList = [];
          // 추후 개설된 방으로 접속하는 로직
          this.findAllRoom();
        } else {
          alert("방 개설 실패");
        }
      });
    },
    onClickImageUpload() {
      this.$refs.imageInput.click();
    },
    onChangeImages(e) {
      console.log(e.target.files);
      const file = e.target.files[0];
      this.imageUrl = URL.createObjectURL(file);
    },
    addHashtag() {
      this.hashtagList.push(this.hashtag);
      this.hashtag = "";
    },
    deleteHashtag(hashtag) {
      const index = this.hashtagList.indexOf(hashtag);
      this.hashtagList.splice(index, 1);
    },
  },
};
</script>

<style>
</style>