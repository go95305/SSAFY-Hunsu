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
          <v-btn color="red accent-3" dark small fab v-bind="attrs" v-on="on">
            <v-icon>mdi-pencil</v-icon>
          </v-btn>
        </div>
      </template>

      <v-card>
        <!--작성창 상단바-->
        <v-toolbar dark color="black">
          <v-btn icon dark @click="dialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title class="text-subtitle-1"
            >뭘입을까 작성</v-toolbar-title
          >
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <v-btn text @click="createWhatWear()" class="text-subtitle-1">
              완료
            </v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <v-list three-line subheader>
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
                counter="250"
                label="내용"
              ></v-textarea>
            </v-list-item-content>
          </v-list-item>
        </v-list>
        <!--사진업로드-->
        <v-list three-line subheader>
          <v-list-item>
            <v-list-item-content>
              <v-list-item-title class="text-h6 font-weight-bold">
                사진업로드
              </v-list-item-title>
              <ImageUpload />
              <!-- <v-img
                v-if="image" :src="imageUrl" id="test" contain>
              </v-img>
              <v-file-input
                accept="image/*"
                label="File input"
                chips
                clearable
                v-model="image"
                @change="previewImage()"
              ></v-file-input> -->
              <!--투표기능-->
              <div id="votebtn">
                <v-checkbox v-model="vote" @click="vote = true"></v-checkbox>
                <v-list-item-title class="text-h6 font-weight-bold"
                  >투표기능</v-list-item-title
                >
              </div>
              <!-- <v-img
                v-for="(voteImageUrl, idx) in voteImageUrls"
                :key="idx"
                :src="voteImageUrl"
              >
              </v-img>
              <v-file-input
                :disabled="!vote"
                multiple
                chips
                accept="image/*"
                label="File input"
                v-model="voteImage"
                @change="previewVoteImage()"
              ></v-file-input> -->
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
                <v-date-picker v-model="dates" full-width color="black">
                  <v-spacer></v-spacer>
                  <v-btn text color="black" @click="dateDialog = false">
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
                  <v-btn text color="black" @click="timeDialog = false">
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
import { rscApi } from "@/services/api"
import { mapGetters, mapActions, mapMutations } from "vuex";
import ImageUpload from "@/components/module/ImageUpload";

export default {
  name: "WhatWearWrite",
  components: {
    ImageUpload,
  },
  data() {
    return {
      dialog: false,
      notifications: false,
      whatwearTitle: "",
      whatwearContent: "",
      vote: false,
      image: null,
      imageUrl: null,
      voteImage: null,
      voteImageUrls: [],
      rules: {
        required: (v) => !!v || "Required",
        min: (v) => v.trim().length > 0 || "공백안됨",
        titleMax: (v) => v.length <= 30 || "30자이하",
        contentMax: (v) => v.length <= 250 || "250자이하",
      },
      time: "",
      menu2: false,
      timeDialog: false,
      dateDialog: false,
      dates: "",
      isValid: false,
      endtime: "",
      num: 0,
      daycheck: false,
      timecheck: false,
    };
  },
  computed: {
    ...mapGetters(["getNickname", "getUploadImageUrls", "getUploadImageFiles"]),
  },
  methods: {
    ...mapActions(["uploadImage"]),
    ...mapMutations([
      "setUploadImageUrls",
      "setUploadImageFiles",
      "clearUploads",
      "setWhatwearInfo",
    ]),
    createWhatWear() {
      // dialog창 닫기 + 입력데이터 보내기
      this.dialog = false;
      let imageFiles = this.getUploadImageFiles;
      let clearUploads = this.clearUploads;
      console.log("여기야", imageFiles);
      if (this.timeDialog && this.dateDialog) {
        this.endtime = this.dates.concat("T", this.time, ":00");
      }
      // 원래는 투표이미지 갯수인데 우선 투표체크박스 활성화하면 숫자가 3이 들어가도록 구현함
      if (this.vote) {
        this.num = this.getUploadImageFiles.length
      }
      // 투표비활성화 일때 글작성 체크(사진은 부가적이므로 체크제외)
      if (this.vote === false && this.whatwearTitle.length > 0 && this.whatwearTitle.length <= 30 && this.whatwearContent.length > 0 && this.whatwearContent.length <= 250) {
        this.isValid = true
      }
      
      // 투표활성화일때 글작성 체크(제목, 내용, 사진갯수)
      if (this.vote === true && this.whatwearTitle.length > 0 && this.whatwearTitle.length <= 30 && this.whatwearContent.length > 0 && this.whatwearContent.length <= 250 && this.num > 0) {
        this.isValid = true
      }
      
      console.log('널값체크', this.endtime, this.getNickname, this.num, this.isVaild)

      const params = {
        content: this.whatwearContent,
        endtime: this.endtime,
        nickname: "감자탕볶음밥",
        num: this.num,
        title: this.whatwearTitle,
      };
      // 작성폼 초기화
      (this.whatwearTitle = ""), (this.whatwearContent = ""), (this.num = 0);
      rscApi
        .post("/wear", params)
        .then((res) => {
          // console.log('뭘입을까글쓰기성공')
          console.log("resres", res);

          if (imageFiles.length !== 0) {
            console.log("in wear file", imageFiles);
            this.uploadImage({ key: "whatwear/", articleIdx: res.data }).then(
              () => {
                clearUploads();
              }
            );
          }

          // this.uploadImage({
          //   key: "whatwear/",
          //   articleIdx: res.data.whatwaerIdx,
          // }).then(() => {
          //   // this.setUploadImageFiles(this.voteImage);
          //   // this.setUploadImageUrls();
          //   // //추후 개별업로드 필요
          //   // this.uploadImage({ key: "vote/", article: res.data.voteIdx });
          // });

          // console.log(params);
        })
        .catch((err) => {
          console.error(err);
        });
    },
    // previewImage() {
    //   this.imageUrl = URL.createObjectURL(this.image);
    // },
    // previewVoteImage() {
    //   console.log(this.voteImage);
    //   this.voteImage.forEach((e) =>
    //     this.voteImageUrls.push(URL.createObjectURL(e))
    //   );
    //   console.log(this.voteImageUrls);
    // },
    inputDate(dates) {
      this.dates = dates;
      this.daycheck = true
      // console.log(this.dates)
    },
    inputTime(time) {
      this.time = time;
      this.timecheck =true
      // console.log(this.time)
    },
  },
};
</script>

<style>
#votebtn {
  display: flex;
}

#whatwear_writebtn {
  position: fixed;
  bottom: 5%;
  left: 85%;
  z-index: 100;
}
</style>