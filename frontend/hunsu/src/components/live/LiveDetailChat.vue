<template>
  <!-- 라이브 채팅창 (아직 구현안돼서 댓글로 대체해놓음)-->
  <div>
    <!-- 이미지 뷰  -->
    <!-- 개설자 채팅 -->
    <v-container fluid>
      <v-row>
        <li v-for="(msg, i) in publisherMsgs" :key="i">{{ msg }}</li>
      </v-row>
    </v-container>
    <!-- 좋아요 누르기 -->
    <v-btn icon @click="plusLike">
      <v-icon id="heart" color="red">mdi-heart</v-icon>
    </v-btn>
    <!-- 참여자 채팅 -->
    <v-container fluid>
      <v-row>
        <li v-for="(msg, i) in joinerMsgs" :key="i">{{ msg }}</li>
      </v-row>
    </v-container>
    <!-- 참여자 채팅 전송 -->
    <v-container fluid>
      <v-row>
        <v-col class="mb-6">
          <v-text-field
            v-model="msg"
            label="바르고 고운 채팅:)"
            outlined
            rows="2"
            row-height="20"
            append-outer-icon="mdi-chevron-right"
            @click:append-outer="sendMessage"
            @keyup.enter="sendMessage"
          ></v-text-field>
        </v-col>
      </v-row>
    </v-container>
    <v-btn @click="exitChatRoom">종료</v-btn>

    <!--클릭할때만 하트애니메이션 작동되도록-->
    <!-- <v-icon >mdi-heart</v-icon>
    <p class="a">🧡</p>
    <p class="a">🧡</p>
    <p class="a">🧡</p>
    <p class="a">🧡</p>
    <p class="a">🧡</p> -->
  </div>
</template>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { mapGetters } from "vuex";

export default {
  name: "LiveDetailChat",
  data: () => ({
    msg: "",
    userCount: 0,
    likeCount: 0,
    joinerMsgs: [],
    publisherMsgs: [],
    stompClient: "",
    connected: false,
  }),
  computed: {
    ...mapGetters(["getChatRoomDetail", "getNickname"]),
  },
  created() {
    let sock = new SockJS("http://localhost:8082/ws-stomp");
    this.stompClient = Stomp.over(sock);

    const _this = this;
    this.stompClient.connect(
      {},
      function (frame) {
        console.log("frame", frame);
        console.log(_this.getNickname);
        _this.connected = true;
        _this.stompClient.subscribe(
          "/sub/chat/room/" + _this.getChatRoomDetail.roomId,
          function (msg) {
            let recv = JSON.parse(msg.body);
            console.log("recv : ", recv);
            _this.recvMessage(recv);
          },
          { nickname: _this.getNickname } // 채팅 참석자 알림
        );
      },
      function (error) {
        alert("서버연결 실패");
        console.log(error);
      }
    );
  },
  beforeDestroyed() {
    console.log("exitroom");
    this.exitChatRoom();
  },
  methods: {
    exitChatRoom() {
      const _this = this;
      this.stompClient.disconnect(
        () => {
          _this.$router.push("/live");
        },
        { nickname: _this.getNickname }
      );
    },
    plusLike() {
      if (!this.stompClient || !this.connected) {
        console.log("연결안됐는데 왜 좋아요보내?");
        return;
      }
      const _this = this;
      this.stompClient.send(
        "/pub/chat/like",
        JSON.stringify({
          type: "LIKE",
          roomId: _this.getChatRoomDetail.roomId,
          sender: _this.getNickname,
        }),
        { nickname: _this.getNickname }
      );
    },
    sendMessage() {
      if (!this.stompClient || !this.connected) {
        console.log("연결안됐는데 왜 메세지보내?");
        return;
      }
      const _this = this;
      let content = JSON.stringify({
        type: "TALK",
        roomId: _this.getChatRoomDetail.roomId,
        message: _this.msg,
        sender: _this.getNickname,
      });
      this.stompClient.send("/pub/chat/message", content, {
        // 여기선 순서 바꿔줘야함
        nickname: _this.getNickname,
      });
      console.log(
        JSON.stringify({
          type: "TALK",
          roomId: _this.getChatRoomDetail.roomId,
          message: _this.msg,
        })
      );
      this.msg = "";
    },
    recvMessage(recv) {
      this.userCount = recv.userCount;
      this.likeCount = recv.likeCount;
      if (recv.type === "LIKE") {
      } else {
        if (recv.sender === this.getNickname) {
          // 개설자 메세지 일 때
          this.publisherMsgs.unshift({
            type: recv.type,
            sender: recv.sender,
            message: recv.message,
          });
        } else {
          // 참여자 메세지 일 때
          this.joinerMsgs.unshift({
            type: recv.type,
            sender: recv.sender,
            message: recv.message,
          });
        }
      }
    },
  },
};
</script>

<style>
.a {
  position: absolute;
  bottom: -60px;
  width: 50px;
  height: 50px;
}

.a:first-of-type {
  left: 10px;
  animation: bubble 2s 1s linear infinite;
}

.a:nth-of-type(2) {
  left: 10px;
  animation: bubble 3s 1s linear infinite;
}

.a:nth-of-type(3) {
  left: 10px;
  animation: bubble 3.5s 1s linear infinite;
}

.a:nth-of-type(4) {
  left: 10px;
  animation: bubble 3.1s 1s linear infinite;
}

.a:nth-of-type(5) {
  left: 10px;
  animation: bubble 3s 1s linear infinite;
}

@keyframes bubble {
  0% {
    bottom: -100px;
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
  to {
    bottom: 100%;
    opacity: 0;
  }
}
</style>