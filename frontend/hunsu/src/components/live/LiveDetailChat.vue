<template>
  <!-- 라이브 채팅창 (아직 구현안돼서 댓글로 대체해놓음)-->
  <div>
    <!-- 이미지 뷰  -->
    <ImageView :images="getChatRoomDetail.imageUrls" />
    <!-- 개설자 채팅 -->
    <v-container fluid>
      <v-virtual-scroll :items="publisherMsgs" height="90" item-height="30">
        <template v-slot:default="{ item }">
          <v-list-item :key="item">
            {{ item.sender }} - {{ item.message }}
          </v-list-item>
          <!-- <v-row>
          <li v-for="(msg, i) in joinerMsgs" :key="i">{{ msg }}</li>
        </v-row> -->
        </template>
      </v-virtual-scroll>
      <!-- <v-row>
        <li v-for="(msg, i) in publisherMsgs" :key="i">{{ msg }}</li>
      </v-row> -->
    </v-container>
    <!-- 좋아요 누르기 -->
    <v-btn icon @click="plusLike">
      <!-- <v-icon class="a" color="red">mdi-heart</v-icon> -->
      <transition name="heart">
        <v-icon v-if="show" class="a heart" color="red">mdi-heart</v-icon>
      </transition>
    </v-btn>
    <!-- 참여자 채팅 -->
    <v-container fluid>
      <!--참가자 채팅 -->
      <v-virtual-scroll :items="joinerMsgs" height="90" item-height="30">
        <template v-slot:default="{ item }">
          <v-list-item :key="item">
            {{ item.sender }} - {{ item.message }}
          </v-list-item>
          <!-- <v-row>
          <li v-for="(msg, i) in joinerMsgs" :key="i">{{ msg }}</li>
        </v-row> -->
        </template>
      </v-virtual-scroll>
    </v-container>
    <!-- 채팅 입력 -->
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
    <v-btn @click="imageUpdate">이미지 수정</v-btn>
  </div>
</template>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import ImageView from "@/components/module/ImageView";
import { mapGetters } from "vuex";

export default {
  name: "LiveDetailChat",
  components: {
    ImageView,
  },
  data: () => ({
    msg: "",
    userCount: 0,
    likeCount: 0,
    joinerMsgs: [],
    publisherMsgs: [],
    stompClient: "",
    connected: false,
    show: true,
  }),
  computed: {
    ...mapGetters(["getChatRoomDetail", "getNickname"]),
  },
  created() {
    let sock = new SockJS("http://i4c102.p.ssafy.io:8082/api/ws-stomp");
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
      this.show = !this.show;
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

      this.stompClient.send(
        "/pub/chat/message",
        JSON.stringify({
          type: "TALK",
          roomId: _this.getChatRoomDetail.roomId,
          message: _this.msg,
          sender: _this.getNickname,
        }),
        {
          // 여기선 순서 바꿔줘야함
          nickname: _this.getNickname,
        }
      );
      this.msg = "";
    },
    imageUpdate() {
      const _this = this;
      this.stompClient.send(
        "/pub/chat/message",
        JSON.stringify({
          type: "IMAGE",
          roomId: _this.getChatRoomDetail.roomId,
          sender: _this.getNickname,
        }),
        {
          nickname: _this.getNickname,
        }
      );
    },
    recvMessage(recv) {
      this.userCount = recv.userCount;
      this.likeCount = recv.likeCount;
      if (recv.type === "LIKE") {
        // console.log(recv);
        this.show = !this.show;
      } else if (recv.type === "IMAGE") {
        console.log("IMAGE tlsgh");
        //이미지 리로드
      } else {
        if (recv.sender === this.getChatRoomDetail.publisher) {
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
  float: center;
  position: absolute;
  width: 50px;
  height: 50px;
}
.heart-enter-active {
  animation: bubble 2s;
}
/* .a:first-of-type {
  animation: bubble 1s 2s linear;
}

.a:nth-of-type(2) {
  animation: bubble 1s 1s linear;
}

.a:nth-of-type(3) {
  animation: bubble 3.5s 1s linear infinite;
}

.a:nth-of-type(4) {
  animation: bubble 3.1s 1s linear infinite;
}

.a:nth-of-type(5) {
  animation: bubble 3s 1s linear infinite;
} */

@keyframes bubble {
  from {
    bottom: 0px;
    left: 0%;
    right: 0%;
    opacity: 1;
  }
  20% {
    right: 20%;
  }
  40% {
    left: -20%;
  }
  60% {
    right: 20%;
  }
  80% {
    left: -20%;
  }
  to {
    bottom: 100px;
    opacity: 0;
  }
}
</style>