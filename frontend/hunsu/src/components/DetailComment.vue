<template>
<!-- OOTD, WHATWEAR 공통 댓글창 -->
  <div>
    <v-card
      flat>
      <v-list three-line>
          <v-subheader
            v-text="getWhatwearReplyInfo.data.length"
          ></v-subheader>
          <!--댓글-->
        <template v-for="(reply, index) in getWhatwearReplyInfo.data">
          <!--댓글출력되는부분-->
          <v-list-item
            v-if="reply"
            :key="index"
          >
          <!--프로필이미지-->
            <v-list-item-avatar>
              <v-img
              src="https://cdn.vuetifyjs.com/images/john.jpg"
              alt="John"
              width="100"
              ></v-img>
            </v-list-item-avatar>
            <!--title자리가 댓글, sub가 좋아요 수정 자리-->
            <v-list-item-content>
              <v-list-item-title v-html="reply.content"></v-list-item-title>
              <!-- <v-list-item-subtitle v-html="item.subtitle"></v-list-item-subtitle> -->
            </v-list-item-content>
          </v-list-item>
        </template>
      </v-list>
    </v-card>
    <v-container fluid>
      <v-row>
        <v-col
          cols="12"
          sm="6"
        >
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
// import axios from 'axios'

export default {
  name: "DetailComment",
  data: () => ({
    items: [
      { header: '댓글' },
    ],
    replyContent: '',
  }),
  computed: {
    ...mapGetters(["getWhatwearInfo","getWhatwearReplyInfo"])
  },
  methods: {
    ...mapMutations(["setWhatwearReplyInfo"]),
    ...mapActions(["createWhatwearReplyInfo"]),
    createWhatwearReply(wear_idx) {
      this.createWhatwearReplyInfo({  
        content: this.replyContent,
        depth: 0,
        groupNum: 0,
        nickname: 'lee',
        wear_idx: wear_idx,
      });
      this.replyContent = ''
      console.log(wear_idx)
    }
  }
}
</script>

<style>

</style>