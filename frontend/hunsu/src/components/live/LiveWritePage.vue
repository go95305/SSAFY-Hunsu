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
        <ImageUpload />
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
// import axios from "axios";
import { mapActions, mapGetters } from "vuex";
import ImageUpload from "@/components/module/ImageUpload";

export default {
  name: "LiveWritePage",
  components: {
    ImageUpload,
  },
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
  computed: {
    ...mapGetters(["getChatRooms"]),
  },

  methods: {
    ...mapActions(["createRoom", "findAllRoom", "uploadImage", "getImageList"]),
    async create() {
      //작성 시 닉네임 체크
      if (this.getNickname == null || this.getNickname == undefined) {
        alert("로그인 후 다시 시도해주세요!");
        this.$router.push("/login");
      }
      const _this = this;
      await this.createRoom({
        title: this.title,
        hashtagList: this.hashtagList,
      }).then((res) => {
        if (res.data) {
          this.dialog = false;
          this.title = "";
          this.hashtagList = [];
          this.uploadImage({
            key: "live/",
            articleIdx: res.data.roomId,
          });
          // 추후 개설된 방으로 접속하는 로직
          this.findAllRoom().then(() => {
            this.getChatRooms.map((room) => {
              _this
                .getImageList({ prefix: "live/" + room.roomId })
                .then((res) => {
                  room.images = res;
                });
            });
          });
        } else {
          alert("방 개설 실패");
        }
      });
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