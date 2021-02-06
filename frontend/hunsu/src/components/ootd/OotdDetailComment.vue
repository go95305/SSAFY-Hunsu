<template>
  <div>
    <div v-for="(reply, groupNum) in getOotdReplyInfo" :key="groupNum">
      <!--댓글창-->
      <v-card v-if="reply.isDeleted" flat class="d-flex align-center justify-space-around">
        <div class="d-flex">
          <div>
            <v-avatar class="mt-5">
              <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" />
            </v-avatar>
          </div>
          <div style="margin: 17px; margin-left: 10px;">
            <p style="margin-bottom: 0; font-size: 14px">{{ reply.nickname }}</p>
            <p style="margin-bottom: 0; font-size: 13px">{{ reply.content }}</p>
          <div class="d-flex">
            <!--write_date가 null이라서 바로반영못함-->
            <!-- <p style="margin-bottom: 0; font-size: 10px">{{ reply.write_date.slice(0, 10) }}</p> -->
            <p style="margin-bottom: 0; font-size: 10px">좋아요 {{ reply.likeCount }}개</p>
            <!-- <p style="margin-bottom: 0; margin-left: 10px; font-size: 10px" @click="clickWhatwearReReply(reply.nickname, reply.groupNum)">답글하기</p> -->
            <p style="margin-bottom: 0; margin-left: 10px; font-size: 10px" @click="clickOotdReply(reply.nickname, reply.groupNum)">수정</p>
            <p style="margin-bottom: 0; margin-left: 10px; font-size: 10px" @click="deleteOotdReply(reply.replyIdx)">삭제</p>
          </div>
          </div>
        </div>
          <v-btn icon @click="likeOotdReply(reply.replyIdx)" :color="reply.like ? 'red' : 'black'"><v-icon>mdi-heart</v-icon></v-btn>
          <!-- <v-btn icon @click="deleteWhatwearReply(reply.idx)"><v-icon>mdi-close</v-icon></v-btn> -->
      </v-card>

    </div>

    <v-container fluid>
      <v-row>
        <v-col cols="12" sm="6">
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
  }),
  computed: {
    ...mapGetters(["getOotdInfo", "getOotdReplyInfo"]),
    
  },
  methods: {
    ...mapMutations(["setOotdReplyInfo"]),
    ...mapActions(["createOotdReplyInfo", "likeOotdReplyInfo", "deleteOotdReplyInfo"]),
    // 댓글작성함수
    createOotdReply(ootd_idx) {
      this.createOotdReplyInfo({
        content: this.replyContent,
        depth: this.depth,
        groupNum: this.groupNum,
        nickname: "han",
        ootdIdx: ootd_idx,
      });
      this.replyContent = "";
      this.depth = 0
      this.groupNum = 0
      console.log(this.getOotdReplyInfo)
    },
    // 댓글좋아요 함수
    likeOotdReply(replyIdx) {
      const nickname = "lee"
      this.likeOotdReplyInfo(replyIdx, nickname)
      console.log(this.getOotdReplyInfo)
    },

    deleteOotdReply(replyIdx) {
      const result = this.deleteOotdReplyInfo(replyIdx)
      if (result) {
        console.log('삭제됨')
        console.log(this.getOotdReplyInfo)
      } else {
        console.log('삭제실패')
      }
    }
  },
};
</script>

<style>

</style>