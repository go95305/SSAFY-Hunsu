<template>
  <!-- 라이브 메인페이지 -->
  <div>
    <!-- <div class="row">
      <div class="col-md-6">
        <h3>채팅방 리스트</h3>
      </div>
      <div class="col-md-6 text-right">
        <a class="btn btn-primary btn-sm" href="/chat/room">방 나가기</a>
      </div>
    </div>
    <div class="input-group">
      <div class="input-group-prepend">
        <label class="input-group-text">방제목</label>
        <input type="text" class="form-control" v-model="room_name" />
      </div>
      <div class="input-group-prepend">
        <label class="input-group-text">해시태그</label>
        <input type="text" class="form-control" v-model="hashtagList" />
      </div>
      <div class="input-group-prepend">
        <label class="input-group-text">고정댓글</label>
        <input type="text" class="form-control" v-model="fixedComment" />
      </div>
      <div class="input-group-append">
        <button class="btn btn-primary" type="button" @click="createRoom">
          채팅방 개설
        </button>
      </div>
    </div>
    <ul class="list-group">
      <li
        class="list-group-item list-group-item-action"
        v-for="item in chatrooms"
        v-bind:key="item.roomId"
      >
        <h6>
          {{ item.name }}
          <span class="badge badge-info badge-pill">{{ item.userCount }}</span
          ><span class="badge badge-info badge-pill">{{ item.likeCount }}</span>
        </h6>
        <span class="badge badge-info badge-pill">{{ item.hashtagList }}</span>
        <br />
        <button
          class="btn btn-primary"
          type="button"
          v-on:click="enterRoom(item.roomId, item.name)"
        >
          방입장
        </button>
        <button
          class="btn btn-primary"
          type="button"
          @click="removeRoom(item.roomId)"
        >
          방 삭제
        </button>
      </li>
    </ul> -->
    <v-card
      v-for="(room, idx) in getChatRooms"
      :key="idx"
      @click="enterRoom(room)"
    >
      <!-- 라이브 메인 사진 -->
      <v-img
        src="https://picsum.photos/350/165?random"
        max-height="125"
        contain
        class="grey darken-4"
      ></v-img>
      <!-- 작성자 프로필 -->
      <v-card-title class="text-body-1" id="writer">
        <v-list-item-avatar>
          <v-img src="https://cdn.vuetifyjs.com/images/john.png"></v-img>
        </v-list-item-avatar>
        {{ room.name }}
        <v-list-item-title class="title text-body-1">
          {{ room.publisher }}
        </v-list-item-title>
      </v-card-title>

      <v-list-item-content>
        <!-- 라이브 제목 -->
        <!-- 해시태그 -->
        <v-list-item-subtitle>
          <!-- 추후 해시태그에 검색 링크 걸 예정 -->
          <p
            v-for="(hashtag, i) in room.hashtagList"
            :key="i"
            style="display: inline"
          >
            {{ "#" + hashtag }}
          </p>
        </v-list-item-subtitle>
      </v-list-item-content>
      <!-- 작성자, 접속자수 -->
    </v-card>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapMutations } from "vuex";

export default {
  name: "LiveList",
  data() {
    return {
      dialog: false,
      notifications: false,
      sound: true,
      widgets: false,
      room_name: "",
      publisher: "",
      hashtagList: [],
      chatrooms: [],
      fixedComment: "",
    };
  },
  created() {
    this.findAllRoom();
  },
  computed: {
    ...mapGetters(["getChatRooms"]),
  },
  methods: {
    ...mapActions(["findAllRoom"]),
    ...mapMutations(["setChatRoomDetail"]),
    // removeRoom(roomId) {
    //   axios.post("/chat/room/remove/" + roomId).then(() => {
    //     console.log("삭제완료");
    //     this.findAllRoom();
    //   });
    // },
    enterRoom(roomInfo) {
      // localStorage.setItem("wschat.roomId", roomId);
      // localStorage.setItem("wschat.roomName", roomName);
      console.log(roomInfo);
      this.setChatRoomDetail(roomInfo);
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
