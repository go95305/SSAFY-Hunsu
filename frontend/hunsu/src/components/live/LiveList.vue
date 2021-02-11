<template>
  <!-- 라이브 메인페이지 -->
  <div>
    <v-card @click="goToLiveDetail">
      <!-- 라이브 메인 사진 -->
      <v-img
        src="https://picsum.photos/350/165?random"
        max-height="125"
        contain
        class="grey darken-4"
      ></v-img>
      <!-- 라이브 제목 -->
      <v-card-title class="title text-body-1">
        한시간 뒤에 소개팅가요 ㅠㅠ 도와주세요
      </v-card-title>
      <!-- 작성자, 접속자수 -->
      <div class="d-flex align-center justify-center">
        <v-list-item-avatar>
          <v-img src="https://cdn.vuetifyjs.com/images/john.png"></v-img>
        </v-list-item-avatar>
        <v-card-title class="text-body-1" id="writer"> 작성자 </v-card-title>
      </div>
    </v-card>
  </div>
</template>

<script>
import axios from "axios";
axios.defaults.url = "http://localhost:8080";
export default {
  name: "LiveList",
  data() => ({
    
      dialog: false,
      notifications: false,
      sound: true,
      widgets: false,
      room_name: "",
    publisher: "",
    hashtagList: [],
    chatrooms: [],
    fixedComment: "",
  },
  created() {
    this.findAllRoom();
  },
  
  methods: {
    findAllRoom: () => {
      axios.get("/chat/rooms").then((res) => {
        if (Object.prototype.toString.call(res.data) === "[object Array]") {
          console.log(res.data);
        }
        this.chatrooms = res.data;
      });
    },
    removeRoom: (roomId) => {
      axios.post("/chat/room/remove/" + roomId).then(() => {
        console.log("삭제완료");
        this.findAllRoom();
      });
    },
    createRoom: () => {
      if ("" === this.room_name) {
        alert("방 제목을 입력해라");
        return;
      } else {
        axios
          .post("/chat/room", {
            name: this.room_name,
            publisher: this.publisher,
            hashtagList: this.hashtagList,
            fixedComment: this.fixedComment,
          })
          .then((res) => {
            alert(res.data.name + " 방 개설 성공");
            this.room_name = "";
            this.hashtagList = [];
            this.fixedComment = "";
            this.findAllRoom();
          })
          .catch((res) => {
            alert("채팅방개설 실패");
            console.log(res);
          });
      }
    },
    enterRoom: (roomId, roomName) => {
      localStorage.setItem("wschat.roomId", roomId);
      localStorage.setItem("wschat.roomName", roomName);
      this.$router.push("/live/detail");
    },
    // goToLiveDetail: () => {
    //   this.$router.push("/live/detail");
    // },
  },
};
</script>

<style scoped>
</style>
