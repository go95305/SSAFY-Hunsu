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

          <!-- 이미지 업로드 -->
          <input
            ref="imageInput"
            type="file"
            multiple
            hidden
            @change="onChangeImages"
          />
          <div class="display: inline-block;" v-if="imageUrls.length === 0">
            <v-btn class="mx-5" type="button" @click="onClickImageUpload"
              >사진 업로드</v-btn
            >
          </div>

          <!-- <v-sheet class="mx-auto" elevation="3" max-width="300"> -->
          <!-- 현재는 사진 업로드만, 올렸던 사진 삭제는 미구현상태 -->
          <div v-else>
            <v-slide-group
              v-model="imageUrls"
              class="pa-4"
              center-active
              show-arrows
            >
              <v-slide-item v-for="(imageUrl, idx) in imageUrls" :key="idx">
                <!-- <v-card
                  :color="active ? 'primary' : 'grey lighten-1'"
                  class="ma-4"
                  height="100"
                  width="100"
                  @click="toggle"
                > -->
                <!-- <div
                    class="file-close-button"
                    style="display: float"
                    @click="fileDeleteButton(idx)"
                    :idx="idx"
                  >
                    x
                  </div> -->
                <v-img
                  class="mx-5 my-5"
                  :src="imageUrl"
                  height="100"
                  width="100"
                >
                </v-img>

                <!-- <v-row class="fill-height" align="center" justify="center">
                  <v-scale-transition>
                    <v-icon
                      v-if="active"
                      color="white"
                      size="48"
                      v-text="'mdi-close-circle-outline'"
                    ></v-icon>
                  </v-scale-transition>
                </v-row> -->
                <!-- </v-card> -->
              </v-slide-item>
              <div>
                <br />
                <v-btn
                  class="mx-5"
                  type="button"
                  @click="onClickImageUpload"
                  width="100"
                  height="100"
                  >추가 사진 업로드</v-btn
                >
                <input
                  type="file"
                  id="file"
                  ref="imageInput"
                  @change="onChangeImages"
                  multiple
                  hidden
                />
              </div>
            </v-slide-group>
          </div>
          <!-- </v-sheet> -->
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
  computed: {
    ...mapGetters(["getNickname"]),
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
        contentMax: (v) => v.length <= 300 || "300자이하",
      },
      imageUrls: [],
      ootd_content: "",
      ootd_hashtag: "",
      ootd_hashtag_array: [],
      imageFiles: [],
    };
  },
  methods: {
    ...mapActions(["createOotdInfo", "getOotdInfoInApi", "uploadImage"]),
    onClickImageUpload() {
      this.$refs.imageInput.click();
    },
    onChangeImages(e) {
      console.log(e.target.files);
      // this.imageFile = e.target.files[0];
      // console.log("onchange", this.imageFile);
      // this.imageUrl = URL.createObjectURL(this.imageFile);
      this.imageFiles = e.target.files;
      console.log("onChange", this.imageFiles);
      let imageUrls = this.imageUrls;
      this.imageFiles.forEach((imageFile) => {
        imageUrls.push(URL.createObjectURL(imageFile));
      });
      // this.imageUrls = this.imageUrls + imageTmp;
      console.log("onCHange imageURl ", this.imageUrls);
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
      let uploadImage = this.uploadImage;
      let imageFiles = this.imageFiles;
      console.log("before send", imageFiles);
      // Ootd 글 내용들
      const params = {
        content: this.ootd_content,
        hashtagList: this.ootd_hashtag_array,
        nickName: this.getNickname,
      };
      this.createOotdInfo(params).then((res) => {
        if (!res) {
          console.log("글 작성 실패");
        } else {
          // 이미지 업로드
          if (imageFiles.length !== 0) {
            console.log("in ootd file", imageFiles);
            // let fileExt =
            imageFiles.forEach((imageFile, idx) => {
              console.log("upload ", imageFile);
              uploadImage({
                key: "ootd/" + res.ootdIdx + "/" + idx + 1 + ".png",
                file: imageFile,
              }).then((res) => {
                console.log("imageupload 1", res);
              });
            });
          } else {
            console.log("file X");
          }
        }
      });
      // 추후 자기가 쓴 페이지로 이동하는 것 수정 요망
      this.ootd_hastag_array = [];
    },
  },
  fileDeleteButton(e, idx) {
    console.log("delet ", idx);
    const targetIdx = e.target.getAttribute("idx");
    this.imageFiles = this.imageFIles.filter((data, idx) => idx !== targetIdx);
    // console.log(this.files);
  },
};
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