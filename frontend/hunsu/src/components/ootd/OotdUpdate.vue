<template>
  <!-- OOTD 작성 페이지 (연필모양 버튼 포함) -->
  <v-row justify="center" class="d-inline-block">
    <v-dialog
      v-model="dialog"
      fullscreen
      hide-overlay
      transition="dialog-bottom-transition"
    >
      <!-- <template v-slot:activator="{ on, attrs }"> -->
      <!-- 연필모양 버튼 (클릭하면 이 페이지가 뜸) -->
      <!-- <v-btn color="red accent-3" dark small fab v-bind="attrs" v-on="on">
          <v-icon>mdi-pencil</v-icon>
        </v-btn>
      </template> -->

      <v-card>
        <!-- 카드 상단 -->
        <v-toolbar dark color="black">
          <!-- 닫힘버튼 -->
          <v-btn icon dark @click="dialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>OOTD 수정</v-toolbar-title>
          <v-spacer></v-spacer>
          <!-- 작성완료 버튼 -->
          <v-toolbar-items>
            <v-btn dark text @click="updateOotdInfo()"> Save </v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <!-- 카드 본문 -->
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
            <!-- 본문 입력 -->
            <v-textarea
              v-model="ootd_content"
              clearable
              clear-icon="mdi-close-circle"
              :rules="[rules.required, rules.min, rules.contentMax]"
              counter="300"
              label="내용"
              class="px-5"
              >{{ ootd_content }}</v-textarea
            >
            <!-- 해쉬태그 입력 -->
            <v-text-field
              label="해시태그 추가"
              @keydown.enter="addHashtag()"
              v-model="ootd_hashtag"
              class="px-5"
            ></v-text-field>
            <div>
              <v-chip
                v-for="(hashtag, idx) in ootd_hashtag_array"
                :key="idx"
                class="ma-2"
                close
                color="red"
                text-color="white"
                @click="deleteHashtag(hashtag)"
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
import axios from "axios";
import { mapGetters, mapMutations } from "vuex";

export default {
  name: "OotdUpdate",
  computed: {
    ...mapGetters(["getOotdInfo"]),
  },
  props: ["dialog"],
  mounted() {
    const ootdInfo = this.getOotdInfo;
    this.ootdContent = ootdInfo.content;
    // this.ootdHashtag = ootdInfo.hashTag;
    this.ootdHashtagArray = ootdInfo.hashTag;
    console.log("Ootd Update", this.ootdContent);
  },
  data() {
    return {
      // dialog: false,
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
  methods: {
    ...mapMutations(["setOotdInfo"]),
    ...mapActions(["updateOotdInfo"]),
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
    // updateOotd() {
    //   this.dialog = false;
    //   console.log(this.ootd_hashtag_array);
    //   const params = {
    //     content: this.ootd_content,
    //     hashtagList: this.ootd_hashtag_array,
    //     nickName: "test",
    //   };

    //   axios
    //     .post("http://localhost:8080/ootd", params)
    //     .then(() => {
    //       console.log("글쓰기성공");
    //       this.ootd_hashtag_array = [];
    //     })
    //     .catch((err) => {
    //       console.error(err);
    //     });
    // },
  },
};
</script>

<style>
</style>