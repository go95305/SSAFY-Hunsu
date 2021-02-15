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
        <ImageUpload />
        <v-divider></v-divider>
        <v-list three-line subheader>
          <div>
            <v-textarea
              color="deep-purple accent-1"
              v-model="ootd_content"
              clearable
              clear-icon="mdi-close-circle"
              :rules="[rules.required, rules.min, rules.contentMax]"
              counter="250"
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
import { mapActions, mapGetters, mapMutations } from "vuex";
import ImageUpload from "@/components/module/ImageUpload";
// 작성값 Null 체크
//
export default {
  name: "OotdWritePage",
  components: {
    ImageUpload,
  },
  data() {
    return {
      dialog: false,
      notifications: false,
      sound: true,
      widgets: false,
      rules: {
        required: (v) => !!v || "Required",
        min: (v) => v.trim().length > 0 || "공백안됨",
        contentMax: (v) => v.length <= 250 || "250자이하",
      },
      imageUrls: [],
      ootd_content: "",
      ootd_hashtag: "",
      ootd_hashtag_array: [],
      imageFiles: [],
      isValid: false,
    };
  },
  computed: {
    ...mapGetters(["getNickname", "getUploadImageUrls", "getUploadImageFiles"]),
  },
  methods: {
    ...mapActions(["createOotdInfo", "getOotdInfoInApi", "uploadImage"]),
    ...mapMutations(["clearUploads"]),

    addHashtag() {
      this.ootd_hashtag_array.push(this.ootd_hashtag);
      this.ootd_hashtag = "";
    },
    deleteHashtag(hashtag) {
      const index = this.ootd_hashtag_array.indexOf(hashtag);
      this.ootd_hashtag_array.splice(index, 1);
    },
    createOotd() {
      // let uploadImage = this.uploadImage;
      let imageFiles = this.getUploadImageFiles;
      let clearUploads = this.clearUploads;
      console.log("before send", imageFiles);
      console.log("사진갯수", this.getUploadImageFiles.length)
      // Ootd 글 내용들
      const params = {
        content: this.ootd_content,
        hashtagList: this.ootd_hashtag_array,
        nickName: this.getNickname,
      };

      // 파일이 하나이상, 0 < 글자수 <= 250
      if ( 
        this.getUploadImageFiles.length > 0 &&
        this.ootd_content.length <= 250 &&
        this.ootd_content.length > 0 
      ) {
        // 아무것도 없는 공백막아주기
        if (this.ootd_content.replace(/(\s*)/g,"") > 0) {
          this.isValid = true
        } 
      }

      if (this.isValid) {
        this.dialog = false;
        this.createOotdInfo(params).then((res) => {
          if (!res) {
            console.log("글 작성 실패");
          } else {
            // 이미지 업로드
            console.log("글 작성 성공 res");
            if (imageFiles.length !== 0) {
              console.log("in ootd file", imageFiles);
              this.uploadImage({ key: "ootd/", articleIdx: res.ootdIdx }).then(
                () => {
                  clearUploads();
                }
              );
  
              // this.$router.go(this.$router.currentRoute); // 현재 페이지 리로드
            } else {
              // 이미지 업로드
              console.log('글 작성 성공')
              if (imageFiles.length !== 0) {
                console.log("in ootd file", imageFiles);
                this.uploadImage({ key: "ootd/", articleIdx: res.ootdIdx }).then(
                  () => {
                    clearUploads();
                  }
                );
    
                // this.$router.go(this.$router.currentRoute); // 현재 페이지 리로드
              } else {
                console.log("file X");
              }
            }
          }});
          // 추후 자기가 쓴 페이지로 이동하는 것 수정 요망
          this.ootd_hastag_array = [];
      
      this.$router.push({name: 'OotdDetail' }).catch(() => {})
      this.isValid = false  
      }
      },
  fileDeleteButton(e, idx) {
    console.log("delete ", idx);
    const targetIdx = e.target.getAttribute("idx");
    this.imageFiles = this.imageFIles.filter((data, idx) => idx !== targetIdx);
  },
    },
  }
</script>

<style>
/* #imageTab {
  white-space: nowrap;
  overflow: auto;
  text-align: center;
}
#imageTab v-img {
  display: inline-block;
  border: 1px solid #6e1d1d;
} */
</style>