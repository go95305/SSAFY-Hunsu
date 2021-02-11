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
    <font-awesome-icon icon="fa-heart" @click="plusLike" />
    <!-- 참여자 채팅 -->
    <v-container fluid>
      <v-row>
        <li v-for="(msg, i) in joinerMsgs" :key="i">{{ msg }}</li>
      </v-row>
    </v-container>
    <!-- 참여자 채팅 전송 -->
    <v-container fluid>
      <v-row>
        <v-col cols="12" sm="6" id="comment_input">
          <v-text-field
            v-model="msg"
            label="바르고 고운 채팅:)"
            outlined
            rows="3"
            row-height="25"
          ></v-text-field>
          <font-awesome-icon icon="fa-angle-right" @click="plusLike" />
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

var sock = new SockJS("http://localhost:8082/ws-stomp");
var ws = Stomp.over(sock);

export default {
  name: "LiveDetailChat",
  data: () => ({
    msg: "",
    userCount: 0,
    likeCount: 0,
    joinerMsgs: [],
    publisherMsgs: [],
  }),
  computed: {
    ...mapGetters(["getChatRoomDetail", "getNickname"]),
  },
  created() {
    const _this = this;
    ws.connect(
      {},
      function (frame) {
        console.log("frame", frame);
        console.log(_this.getNickname);

        ws.subscribe(
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
  methods: {
    exitChatRoom() {
      const _this = this;
      ws.disconnect(
        () => {
          _this.$router.push("/live");
        },
        { nickname: _this.getNickname }
      );
    },
    plusLike(type) {
      const _this = this;
      ws.send(
        "/pub/chat/like",
        { nickname: _this.getNickname },
        JSON.stringify({
          type: type,
          roomId: _this.getChatRoomDetail.roomId,
          sender: _this.getNickname,
        })
      );
    },
    sendMessage(type) {
      console.log("여기부터");
      const _this = this;
      ws.send(
        "/pub/chat/message",
        { nickname: _this.getNickname },
        JSON.stringify({
          type: type,
          roomId: _this.getChatRoomDetail.roomId,
          message: _this.message,
        })
      );
      this.message = "";
    },
    recvMessage(recv) {
      this.userCount = recv.userCount;
      this.likeCount = recv.likeCount;
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