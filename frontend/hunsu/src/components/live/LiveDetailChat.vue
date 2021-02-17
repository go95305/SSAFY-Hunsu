<template>
  <!-- 라이브 채팅창 (아직 구현안돼서 댓글로 대체해놓음)-->
  <div>
    <!-- 이미지 뷰  -->
    <ImageView :images="getChatRoomDetail.imageUrls" @click.native="plusLike" />
    <!-- 개설자 채팅 -->
    <v-container fluid>
      <v-virtual-scroll :items="publisherMsgs" height="120" item-height="50">
        <template v-slot:default="{ item }">
          <v-list-item class="publisher" :key="item.sender">
            {{ item.sender }} - {{ item.message }}
          </v-list-item>
        </template>
      </v-virtual-scroll>
    </v-container>
    <v-row style="height: 20px; margin-bottom: 0px" no-gutters>
      <!-- 좋아요 누르기 -->
      <v-col cols="12">
        <hr />
      </v-col>
      <v-col cols="11">
        <div style="text-align: center; padding: 0px">
          <v-btn icon @click="plusLike">
            <v-icon v-show="show === 0" color="red">mdi-heart</v-icon>
            <transition-group>
              <v-icon class="a heart" :key="1" v-show="show === 1" color="red"
                >mdi-heart</v-icon
              >
              <v-icon class="a heart" :key="2" v-show="show === 2" color="red"
                >mdi-heart</v-icon
              >
              <v-icon class="a heart" :key="3" v-show="show === 3" color="red"
                >mdi-heart</v-icon
              >
              <v-icon class="a heart" :key="4" v-show="show === 4" color="red"
                >mdi-heart</v-icon
              >
              <v-icon class="a heart" :key="5" v-show="show === 5" color="red"
                >mdi-heart</v-icon
              >
              <v-icon class="a heart" :key="6" v-show="show === 6" color="red"
                >mdi-heart</v-icon
              >
              <v-icon class="a heart" :key="7" v-show="show === 7" color="red"
                >mdi-heart</v-icon
              >
              <v-icon class="a heart" :key="8" v-show="show === 8" color="red"
                >mdi-heart</v-icon
              >
              <v-icon class="a heart" :key="9" v-show="show === 9" color="red"
                >mdi-heart</v-icon
              >
              <v-icon class="a heart" :key="10" v-show="show === 10" color="red"
                >mdi-heart</v-icon
              >
            </transition-group>
          </v-btn>
        </div>
      </v-col>
    </v-row>
    <v-container fluid>
      <!--참가자 채팅 -->
      <v-virtual-scroll :items="joinerMsgs" height="165" item-height="50">
        <template v-slot:default="{ item }">
          <v-list-item
            class="mymessage"
            v-if="item.sender === getNickname"
            :key="item.sender"
          >
            {{ item.sender }} - {{ item.message }}
          </v-list-item>
          <v-list-item v-else class="joiner" :key="item.sender">
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
    <!-- <v-btn @click="exitChatRoom">종료</v-btn>
    <v-btn @click="imageUpdate">이미지 수정</v-btn> -->
  </div>
</template>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import ImageView from "@/components/module/ImageView";
import { mapGetters, mapMutations } from "vuex";

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
    connected: false,
    show: 0,
  }),
  computed: {
    ...mapGetters(["getChatRoomDetail", "getNickname", "getStompClient"]),
  },
  created() {
    console.log(this.getChatRoomDetail);
    let sock = new SockJS("http://i4c102.p.ssafy.io:8082/api/ws-stomp");
    // this.stompClient = Stomp.over(sock);
    this.setStompClient(Stomp.over(sock));

    const _this = this;
    this.getStompClient.connect(
      {},
      function (frame) {
        console.log("frame", frame);
        console.log(_this.getNickname);
        _this.connected = true;
        _this.getStompClient.subscribe(
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
    ...mapMutations(["setStompClient"]),
    exitChatRoom() {
      const _this = this;
      this.getStompClient.disconnect(
        () => {
          _this.$router.push("/live");
        },
        { nickname: _this.getNickname }
      );
    },
    plusLike() {
      if (!this.getStompClient || !this.connected) {
        console.log("연결안됐는데 왜 좋아요보내?");
        return;
      }
      const _this = this;
      this.getStompClient.send(
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
      if (!this.getStompClient || !this.connected) {
        console.log("연결안됐는데 왜 메세지보내?");
        return;
      }
      const _this = this;
      if (this.msg.length === 0) {
        alert("메세지를 입력하세요!");
        return;
      }
      console.log(this.msg);
      this.getStompClient.send(
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
      if (!this.getStompClient || !this.connected) {
        console.log("연결안됐는데 왜 이미지보내?");
        return;
      }
      const _this = this;
      this.getStompClient.send(
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
        this.show++;
        if (this.show == 11) {
          this.show = 0;
        }
        // this.show = !this.show;
      } else if (recv.type === "IMAGE") {
        console.log("IMAGE tlsgh");
        //이미지 리로드
      } else {
        if (recv.sender === this.getChatRoomDetail.nickname) {
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
.heart {
  position: relative;
  /* bottom: -60px; */
  width: 50px;
  height: 50px;
  background: url("https://s3.us-east-2.amazonaws.com/upload-icon/uploads/icons/png/15721583221557740359-512.png")
    no-repeat;
  background-size: cover;
}
/* .heart-enter-active {
  animation: bubble 2s;
} */

.heart:first-of-type {
  /* left: 10px; */
  animation: bubble 0.5s linear;
}

.heart:nth-of-type(2) {
  /* left: 50%; */
  animation: bubble 0.7s linear;
}

.heart:nth-of-type(3) {
  /* left: 150px; */
  animation: bubble 0.5s linear;
}

.heart:nth-of-type(4) {
  /* right: 100px; */
  animation: bubble 1s linear;
}

.heart:nth-of-type(5) {
  /* right: 10px; */
  animation: bubble 0.3s linear;
}

.heart:nth-of-type(6) {
  /* right: 30px; */
  animation: bubble 1s linear;
}
.heart:nth-of-type(7) {
  /* right: 55px; */
  animation: bubble 1s 1s linear;
}
.heart:nth-of-type(8) {
  /* right: 50%; */
  animation: bubble 1s 1s linear;
}
.heart:nth-of-type(9) {
  /* right: 70%; */
  animation: bubble 1s 1s linear;
}
.heart:nth-of-type(10) {
  /* right: 10%; */
  animation: bubble 1s 1s linear;
}

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
.publisher {
  position: relative;
  margin: 10px;
  width: 320px;
  background: #f4f4e8;
  border-radius: 12px;
}
.publisher:after {
  border-top: 10px solid #f4f4e8;
  border-left: 15px solid transparent;
  border-right: 0px solid transparent;
  border-bottom: 0px solid transparent;
  position: absolute;
  top: 10px;
  left: -10px;
}
.joiner {
  position: relative;
  margin: 10px;
  width: 320px;
  background: #e8f4ee;
  border-radius: 12px;
}
.joiner:after {
  border-top: 10px solid #e8f4ee;
  border-left: 15px solid transparent;
  border-right: 0px solid transparent;
  border-bottom: 0px solid transparent;
  position: absolute;
  top: 10px;
  left: -10px;
}
.mymessage {
  position: relative;
  margin: 0px 0px 0px 20px;
  width: 320px;
  background: #e8f4f4;
  border-radius: 12px;
}
.mymessage:after {
  border-top: 15px solid #e8f4f4;
  border-left: 0px solid transparent;
  border-right: 15px solid transparent;
  border-bottom: 0px solid transparent;
  position: absolute;
  top: 10px;
  right: -10px;
}
</style>