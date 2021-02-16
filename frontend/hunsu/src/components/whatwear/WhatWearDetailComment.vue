<template>
  <div>
    <v-container fluid>
      <p>댓글 {{ getReplyCount }}개</p>
      <v-row>
        <v-col cols="12" sm="6" id="comment_input">
          <v-text-field
            label="댓글쓰기"
            outlined
            rows="3"
            row-height="25"
            v-model="replyContent"
            @keydown.enter="createWhatwearReply(getWhatwearInfo.wear_idx)"
            :append-icon="replyContent ? 'mdi-send' : ''"
            @click:append="createWhatwearReply(getWhatwearInfo.wear_idx)"
          ></v-text-field>
        </v-col>
      </v-row>
    </v-container>
    <div v-for="(reply, groupNum) in whatwearReplyInfo" :key="groupNum">
      <!--댓글창-->
      <v-card
        v-if="reply.depth === 0"
        flat
        class="d-flex align-center justify-space-around"
      >
        <div class="d-flex">
          <div>
            <v-avatar class="mt-5">
              <img
                :src="reply.profileImage"
                alt="John"
                @click="goToProfilePage(reply)"
              />
            </v-avatar>
          </div>
          <div style="margin: 17px; margin-left: 10px">
            <p style="margin-bottom: 0; font-size: 14px">
              {{ reply.nickname }}
            </p>
            <p v-if="reply.flag" style="margin-bottom: 0; font-size: 13px">
              {{ reply.content }}
            </p>
            <p v-if="!reply.flag" style="margin-bottom: 0; font-size: 13px">
              작성자에 의해 삭제된 댓글 입니다.
            </p>
            <div class="d-flex">
              <!--write_date가 null이라서 바로반영못함-->
              <!-- <p style="margin-bottom: 0; font-size: 10px">{{ reply.write_date.slice(0, 10) }}</p> -->
              <p style="margin-bottom: 0; font-size: 10px">
                좋아요 {{ reply.count }}개
              </p>
              <p
                style="margin-bottom: 0; margin-left: 10px; font-size: 10px"
                @click="clickWhatwearReReply(reply.nickname, reply.groupNum)"
              >
                답글하기
              </p>
              <p
                v-if="reply.nickname === getNickname"
                style="margin-bottom: 0; margin-left: 10px; font-size: 10px"
                @click="updateWhatwearReply(reply)"
              >
                수정
              </p>
              <p
                v-if="reply.nickname === getNickname"
                style="margin-bottom: 0; margin-left: 10px; font-size: 10px"
                @click="deleteWhatwearReply(reply.idx)"
              >
                삭제
              </p>
            </div>
          </div>
        </div>
        <v-btn
          v-if="reply.like === false"
          icon
          @click="likeWhatwearReply(reply)"
          color="black"
          ><v-icon>mdi-heart-outline</v-icon></v-btn
        >
        <v-btn
          v-if="reply.like === true"
          icon
          @click="likeWhatwearReply(reply)"
          color="red"
          ><v-icon>mdi-heart</v-icon></v-btn
        >
      </v-card>

      <v-card> </v-card>

      <!--대댓글창-->
      <v-card
        v-if="reply.depth === 1 && reply.flag"
        flat
        class="d-flex align-center justify-space-around"
      >
        <div class="d-flex">
          <div>
            <v-avatar class="mt-5 ml-4" width="30" height="30">
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
                좋아요 {{ reply.count }}개
              </p>
              <p
                style="margin-bottom: 0; margin-left: 10px; font-size: 10px"
                @click="clickWhatwearReReply(reply.nickname, reply.groupNum)"
              >
                답글하기
              </p>
              <p
                v-if="reply.nickname === getNickname"
                style="margin-bottom: 0; margin-left: 10px; font-size: 10px"
                @click="updateWhatwearReply(reply)"
              >
                수정
              </p>
              <p
                v-if="reply.nickname === getNickname"
                style="margin-bottom: 0; margin-left: 10px; font-size: 10px"
                @click="deleteWhatwearReply(reply.idx)"
              >
                삭제
              </p>
            </div>
          </div>
        </div>
        <v-btn
          v-if="reply.like === false"
          icon
          @click="likeWhatwearReply(reply)"
          color="black"
          ><v-icon>mdi-heart-outline</v-icon></v-btn
        >
        <v-btn
          v-if="reply.like === true"
          icon
          @click="likeWhatwearReply(reply)"
          color="red"
          ><v-icon>mdi-heart</v-icon></v-btn
        >
      </v-card>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapMutations, mapActions } from "vuex";

export default {
  name: "WhatWearDetailComment",
  data: () => ({
    items: [{ header: "댓글" }],
    replyContent: "",
    depth: 0,
    groupNum: 0,
    update: false,
    updateReplyIdx: 0,
    iconName: "",
    whatwearReplyInfo: [],
  }),
  computed: {
    ...mapGetters([
      "getWhatwearInfo",
      "getWhatwearReplyInfo",
      "getNickname",
      "getReplyCount",
    ]),
  },
  mounted() {
    this.getCommentProfileImages();
  },
  methods: {
    ...mapMutations(["setWhatwearReplyInfo"]),
    ...mapActions([
      "createWhatwearReplyInfo",
      "likeWhatwearReplyInfo",
      "deleteWhatwearReplyInfo",
      "updateWhatwearReplyInfo",
      "getWhatwearProfile",
      "getProfileInfoInApi",
    ]),
    async goToProfilePage(reply) {
      await this.getProfileInfoInApi(reply.nickname);
      window.scrollTo({ top: "0", behavior: "smooth" });
      this.$router.push({ name: "MyPage" });
    },
    async getCommentProfileImages() {
      // 댓글 내 프로필 사진 가져오기
      this.whatwearReplyInfo = this.getWhatwearReplyInfo;
      await this.whatwearReplyInfo.map(async (reply) => {
        const image = await this.getWhatwearProfile(reply.uid);
        // console.log("in reply", image);
        this.$set(reply, "profileImage", image);
      });
    },
    // 댓글작성함수
    async createWhatwearReply(wearIdx) {
      if (this.update === true) {
        await this.updateWhatwearReplyInfo({
          content: this.replyContent,
          nickname: this.getNickname,
          idx: this.updateReplyIdx,
        });
      }
      if (this.update === false) {
        await this.createWhatwearReplyInfo({
          content: this.replyContent,
          depth: this.depth,
          groupNum: this.groupNum,
          nickname: this.getNickname,
          wear_idx: wearIdx,
        });
      }
      await this.getCommentProfileImages();
      this.replyContent = "";
      this.depth = 0;
      this.groupNum = 0;
    },
    // 댓글좋아요 함수
    async likeWhatwearReply(reply) {
      console.log(reply);
      await this.likeWhatwearReplyInfo(reply.idx);
      this.getCommentProfileImages();
    },
    // 대댓글작성함수
    clickWhatwearReReply(nickname, groupNum) {
      this.replyContent = "@" + nickname + " ";
      this.depth = 1;
      this.groupNum = groupNum;
    },
    async deleteWhatwearReply(replyIdx) {
      await this.deleteWhatwearReplyInfo(replyIdx);
      this.getCommentProfileImages();
    },
    updateWhatwearReply(reply) {
      this.replyContent = reply.content;
      this.update = true;
      this.updateReplyIdx = reply.idx;
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