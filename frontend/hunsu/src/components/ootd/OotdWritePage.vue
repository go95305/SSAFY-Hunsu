<template>
  <!-- OOTD 작성 페이지 (연필모양 버튼 포함) -->
  <v-row justify="center" class="d-inline-block">
    <v-dialog
      v-model="dialog"
      fullscreen
      hide-overlay
      transition="dialog-bottom-transition"
    >
      <template v-slot:activator="{ on, attrs }">
        <!-- 연필모양 버튼 (클릭하면 이 페이지가 뜸) -->
        <v-btn color="red accent-3" dark small fab v-bind="attrs" v-on="on">
          <v-icon>mdi-pencil</v-icon>
        </v-btn>
      </template>
      <v-card>
        <v-toolbar dark color="black">
          <v-btn icon dark @click="dialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>OOTD 작성</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <!-- 작성완료 버튼 -->
            <v-btn dark text @click="createOotd()"> Save </v-btn>
          </v-toolbar-items>
        </v-toolbar>
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
        <v-divider></v-divider>
        <v-list three-line subheader>
          <div>
            <v-textarea
              color="deep-purple accent-1"
              v-model="ootd_content"
              clearable
              clear-icon="mdi-close-circle"
              :rules="[rules.required, rules.min, rules.contentMax]"
              counter="300"
              label="내용"
              class="px-5"
            ></v-textarea>
            <v-text-field
              color="deep-purple accent-1"
              label="해시태그 추가"
              @keydown.enter="addHashtag()"
              v-model="ootd_hashtag"
              class="px-5"
            ></v-text-field>
            <div class="mx-4">
              <v-chip
                v-for="(hashtag, idx) in ootd_hashtag_array"
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
          </div>
        </v-list>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
// import axios from "axios";
import { mapActions, mapGetters } from "vuex";
// 작성값 Null 체크
//
export default {
  name: "OotdWritePage",
  data() {
    return {
      dialog: false,
      notifications: false,
      sound: true,
      widgets: false,
      rules: {
        required: (v) => !!v || "Required",
        min: (v) => v.trim().length > 0 || "공백안됨",
        contentMax: (v) => v.length <= 300 || "300자이하",
      },
      imageUrl: null,
      ootd_content: "",
      ootd_hashtag: "",
      ootd_hashtag_array: [],
    };
  },
  computed: { ...mapGetters(["getNickname"]) },
  methods: {
    ...mapActions(["createOotdInfo", "getOotdInfoInApi"]),
    onClickImageUpload() {
      this.$refs.imageInput.click();
    },
    onChangeImages(e) {
      console.log(e.target.files);
      const file = e.target.files[0];
      this.imageUrl = URL.createObjectURL(file);
    },
    addHashtag() {
      this.ootd_hashtag_array.push(this.ootd_hashtag);
      this.ootd_hashtag = "";
    },
    deleteHashtag(hashtag) {
      const index = this.ootd_hashtag_array.indexOf(hashtag);
      this.ootd_hashtag_array.splice(index, 1);
    },
    createOotd() {
      this.dialog = false;
      //   console.log(this.ootd_hashtag_array);
      const params = {
        content: this.ootd_content,
        hashtagList: this.ootd_hashtag_array,
        nickName: this.getNickname,
      };
      if (this.createOotdInfo(params)) {
        this.getOotdInfoInApi(0);
        this.ootd_hastag_array = [];
      } else {
        console.log("실패함");
      }
    },
  },
};
</script>

<style>
</style>