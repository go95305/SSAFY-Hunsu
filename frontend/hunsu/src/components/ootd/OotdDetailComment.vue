<template>
  <div>
    <v-container fluid>
      <v-row>
        <v-col cols="12" sm="6" id="comment_input">
          <v-text-field
            label="댓글쓰기"
            outlined
            rows="3"
            row-height="25"
            v-model="replyContent"
            @keydown.enter="createOotdReply(getOotdInfo.ootdIdx)"
            :append-icon="replyContent ? 'mdi-send' : ''"
            @click:append="createOotdReply(getOotdInfo.ootdIdx)"
          ></v-text-field>
        </v-col>
      </v-row>
    </v-container>
    <div v-for="(reply, groupNum) in ootdReplyInfo" :key="groupNum">
      <!--댓글창-->
      <v-card
        v-if="reply.isDeleted"
        flat
        class="d-flex align-center justify-space-around"
      >
        <div class="d-flex" style="width: 310px">
          <div>
            <v-avatar class="mt-5 ml-2">
              <img :src="reply.profileImage" alt="John" />
            </v-avatar>
          </div>
          <div style="margin: 17px; margin-left: 10px">
            <p style="margin-bottom: 0; font-size: 14px">
              {{ reply.nickname }}
            </p>
            <p style="margin-bottom: 0; font-size: 13px">{{ reply.content }}</p>
            <div class="d-flex">
              <!--write_date가 null이라서 바로반영못함-->
              <!-- <p style="margin-bottom: 0; font-size: 10px">{{ reply.write_date.slice(0, 10) }}</p> -->
              <p style="margin-bottom: 0; font-size: 10px">
                좋아요 {{ reply.likeCount }}개
              </p>
              <!-- <p style="margin-bottom: 0; margin-left: 10px; font-size: 10px" @click="clickWhatwearReReply(reply.nickname, reply.groupNum)">답글하기</p> -->
              <p
                style="margin-bottom: 0; margin-left: 10px; font-size: 10px"
                v-if="reply.nickname === getNickname"
                @click="updateOotdReply(reply)"
              >
                수정
              </p>
              <p
                style="margin-bottom: 0; margin-left: 10px; font-size: 10px"
                v-if="reply.nickname === getNickname"
                @click="deleteOotdReply(reply.replyIdx)"
              >
                삭제
              </p>
            </div>
          </div>
        </div>


        <v-btn
          class="mr-3"
          v-if="reply.like === false"
          icon
          @click="likeOotdReply(reply.replyIdx)"
          color="black"
          ><v-icon>mdi-heart-outline</v-icon></v-btn
        >
        <v-btn
          class="mr-3"
          v-if="reply.like === true"
          icon
          @click="likeOotdReply(reply.replyIdx)"
          color="red"
          ><v-icon>mdi-heart</v-icon></v-btn
        >


        <!-- <v-btn
          class="mr-2"
          icon
          @click="likeOotdReply(reply.replyIdx)"
          :color="reply.like ? 'red' : 'black'"
          ><v-icon>mdi-heart-outline</v-icon></v-btn
        > -->
        <!-- <v-btn icon @click="deleteWhatwearReply(reply.idx)"><v-icon>mdi-close</v-icon></v-btn> -->
      </v-card>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapMutations, mapActions } from "vuex";

export default {
  name: "OotdDetailComment",
  data: () => ({
    items: [{ header: "댓글" }],
    replyContent: "",
    depth: 0,
    groupNum: 0,
    update: false,
    updateReplyIdx: 0,
    ootdReplyInfo: [],
  }),
  computed: {
    ...mapGetters(["getOotdInfo", "getOotdReplyInfo", "getNickname"]),
  },
  async mounted() {
    //프로필 이미지 가져옴
    this.ootdReplyInfo = this.getOotdReplyInfo;
    console.log("info", this.ootdReplyInfo);

    this.getCommentProfileImages();
    // await this.ootdReplyInfo.map(async (reply) => {
    //   const image = await this.getWhatwearProfile(reply.uid);
    //   console.log("in reply", image);
    //   this.$set(reply, "profileImage", image);
    // });
    // console.log(this.getOotdReplyInfo);
  },
  methods: {
    ...mapMutations(["setOotdReplyInfo"]),
    ...mapActions([
      "createOotdReplyInfo",
      "likeOotdReplyInfo",
      "deleteOotdReplyInfo",
      "updateOotdReplyInfo",
      "getWhatwearProfile",
    ]),
    async getCommentProfileImages() {
      // 댓글 내 프로필 사진 가져오기
      this.ootdReplyInfo = this.getOotdReplyInfo;
      await this.ootdReplyInfo.map(async (reply) => {
        const image = await this.getWhatwearProfile(reply.uid);
        this.$set(reply, "profileImage", image);
      });
    },
    // 댓글 작성 및 수정
    async createOotdReply(ootd_idx) {
      if (this.update) {
        await this.updateOotdReplyInfo({
          content: this.replyContent,
          replyIdx: this.updateReplyIdx,
        });
        console.log("수정성공");
      } else {
        await this.createOotdReplyInfo({
          content: this.replyContent,
          depth: this.depth,
          groupNum: this.groupNum,
          nickname: this.getNickname,
          ootdIdx: ootd_idx,
        });

        console.log("댓글작성성공");
      }
      this.getCommentProfileImages();

      this.replyContent = "";
      this.depth = 0;
      this.groupNum = 0;
      this.update = false;
    },
    // 댓글좋아요 함수
    async likeOotdReply(replyIdx) {
      await this.likeOotdReplyInfo(replyIdx);
      this.getCommentProfileImages();
      console.log(this.getOotdReplyInfo);
    },

    //댓글 삭제
    async deleteOotdReply(replyIdx) {
      await this.deleteOotdReplyInfo(replyIdx);
      this.getCommentProfileImages();
    },

    updateOotdReply(reply) {
      this.replyContent = reply.content;
      this.update = true;
      this.updateReplyIdx = reply.replyIdx;
    },
  },
};
</script>

<style>
#comment_input {
  height: 70px;
  margin-bottom: 10px;
}
</style>