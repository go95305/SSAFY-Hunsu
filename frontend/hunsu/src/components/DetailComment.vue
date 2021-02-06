<template>
  <!-- OOTD, WHATWEAR 공통 댓글창 -->
  <div>
    <div v-for="(reply, groupNum) in getWhatwearReplyInfo" :key="groupNum">
      <!--댓글창-->
      <v-card v-if="reply.depth === 0 && reply.flag" flat class="d-flex align-center justify-space-around">
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
            <p style="margin-bottom: 0; font-size: 10px">좋아요 {{ reply.count }}개</p>
            <p style="margin-bottom: 0; margin-left: 10px; font-size: 10px" @click="clickWhatwearReReply(reply.nickname, reply.groupNum)">답글하기</p>
            <p style="margin-bottom: 0; margin-left: 10px; font-size: 10px" @click="updateWhatwearReply(reply)">수정</p>
          </div>
          </div>
        </div>
          <v-btn icon @click="likeWhatwearReply(reply.idx)" :color="reply.like ? 'red' : 'black'"><v-icon>mdi-heart-outline</v-icon></v-btn>
          <v-btn icon @click="deleteWhatwearReply(reply.idx)"><v-icon>mdi-close</v-icon></v-btn>
      </v-card>
      <!--대댓글창-->
      <v-card v-if="reply.depth === 1 && reply.flag" flat class="d-flex align-center justify-space-around">
        <div class="d-flex">
          <div>
            <v-avatar class="mt-5 ml-4" width="30" height="30">
              <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" />
            </v-avatar>
          </div>
          <div style="margin: 17px; margin-left: 10px;">
            <p style="margin-bottom: 0; font-size: 14px">{{ reply.nickname }}</p>
            <p style="margin-bottom: 0; font-size: 13px">{{ reply.content }}</p>
          <div class="d-flex">
            <!--write_date가 null이라서 바로반영못함-->
            <!-- <p style="margin-bottom: 0; font-size: 10px">{{ reply.write_date.slice(0, 10) }}</p> -->
            <p style="margin-bottom: 0; font-size: 10px">좋아요 {{ reply.count }}개</p>
            <p style="margin-bottom: 0; margin-left: 10px; font-size: 10px" @click="clickWhatwearReReply(reply.nickname, reply.groupNum)">답글하기</p>
            <p style="margin-bottom: 0; margin-left: 10px; font-size: 10px" @click="updateWhatwearReply(reply)">수정</p>         
         </div>
          </div>
        </div>
        <v-btn icon @click="likeWhatwearReply(reply.idx)" :color="reply.like ? 'red' : 'black'"><v-icon>mdi-heart-outline</v-icon></v-btn>
        <v-btn icon @click="deleteWhatwearReply(reply.idx)"><v-icon>mdi-close</v-icon></v-btn> 
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
            @keydown.enter="createWhatwearReply(getWhatwearInfo.wear_idx)"
            :append-icon="replyContent ? 'mdi-send' : ''"
            @click:append="createWhatwearReply(getWhatwearInfo.wear_idx)"
          ></v-text-field>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import { mapGetters, mapMutations, mapActions } from "vuex";

export default {
  name: "DetailComment",
  data: () => ({
    items: [{ header: "댓글" }],
    replyContent: "",
    depth: 0,
    groupNum: 0,
  }),
  computed: {
    ...mapGetters(["getWhatwearInfo", "getWhatwearReplyInfo"]),
    
  },
  methods: {
    ...mapMutations(["setWhatwearReplyInfo"]),
    ...mapActions(["createWhatwearReplyInfo", "likeWhatwearReplyInfo", "deleteWhatwearReplyInfo", "updateWhatwearReplyInfo"]),
    // 댓글작성함수
    createWhatwearReply(wearIdx) {
      this.createWhatwearReplyInfo({
        content: this.replyContent,
        depth: this.depth,
        groupNum: this.groupNum,
        nickname: "han",
        wear_idx: wearIdx,
      });
      this.replyContent = "";
      this.depth = 0
      this.groupNum = 0
    },
    // 댓글좋아요 함수
    likeWhatwearReply(replyIdx) {
      const nickname = "lee"
      this.likeWhatwearReplyInfo(replyIdx, nickname)
    },
    // 대댓글작성함수
    clickWhatwearReReply(nickname, groupNum) {
      this.replyContent = '@'+nickname+' '
      this.depth = 1
      this.groupNum = groupNum
    },
    deleteWhatwearReply(replyIdx) {
      console.log(replyIdx)
      const result = this.deleteWhatwearReplyInfo(replyIdx)
      if (result) {
        console.log('삭제됨')
      } else {
        console.log('삭제실패')
      }
    },
    updateWhatwearReply(reply) {
      this.replyContent = reply.content
      this.updateWhatwearReplyInfo({
        content: this.replyContent,
        replyIdx: reply.idx,
      })
    },
  },
};
</script>

<style>

</style>