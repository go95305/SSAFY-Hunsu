<template>
  <!-- 라이브 디테일 -->
  <v-card>
    <v-list two-line>
      <!-- 뒤로가기 버튼 (라이브 메인페이지로 이동) -->
      <div class="d-flex align-center justify-space-between">
        <div>
          <v-btn icon @click="goToLiveMain">
            <v-icon>mdi-chevron-left</v-icon>
          </v-btn>
        </div>
        <!-- 라이브 제목 -->
        <div class="text-truncate">{{ getChatRoomDetail.title }}</div>
        <!--개설자면 종료, 참여자는 나가기로 표시-->
        <v-btn
          text
          v-if="getChatRoomDetail.nickname === getNickname"
          class="mb-1"
          @click="endRoom"
          >종료</v-btn
        >
        <v-btn text v-else class="mb-1" @click="exitRoom">방 나가기</v-btn>
      </div>
      <v-list-item style="height: 50px">
        <v-list-item-avatar>
          <v-img :src="getChatRoomDetail.profileImage"></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
          <v-list-item-title>{{
            getChatRoomDetail.nickname
          }}</v-list-item-title>
        </v-list-item-content>
        <v-list-item-action>
          <div class="d-flex align-center">
            <v-icon color="red">mdi-circle-medium</v-icon>
            <v-list-item-subtitle
              >참여자수 :
              {{ getChatRoomDetail.userCount + 1 }}</v-list-item-subtitle
            >
          </div>
        </v-list-item-action>
      </v-list-item>
    </v-list>
    <LiveDetailChat />
  </v-card>
</template>

<script>
import LiveDetailChat from "@/components/live/LiveDetailChat";
import { mapGetters, mapActions } from "vuex";
export default {
  components: {
    LiveDetailChat,
  },
  data() {
    return {
      items: [
        {
          text: "수정",
        },
        {
          text: "삭제",
        },
      ],
      cycle: false,
    };
  },
  computed: {
    ...mapGetters(["getChatRoomDetail", "getStompClient", "getNickname"]),
  },
  methods: {
    ...mapActions(["endMyRoom"]),
    goToLiveMain() {
      this.$router.push("/live");
    },
    goToPage(item) {
      // console.log(item.text)
      if (item.text === "MyPage") {
        this.$router.push("/mypage");
      }
    },
    goToLogin() {
      this.$router.push("/login");
    },
    endRoom() {
      this.exitRoom();
      this.endMyRoom();
    },
    exitRoom() {
      const _this = this;
      this.getStompClient.disconnect(
        () => {
          _this.$router.push("/live");
        },
        { nickname: _this.getNickname }
      );
    },
  },
};
</script>

<style>
</style>