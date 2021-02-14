<template>
  <!-- 라이브 메인페이지 -->
  <div v-if="imageReady">
    <v-card
      v-for="(room, idx) in getChatRooms"
      :key="idx"
      @click="enterRoom(room)"
    >
      <!-- 라이브 메인 사진 -->
      <!-- <ImageView :images="room.imageUrls" /> -->
      <ImageView :images="room.imageUrls" />
      <!-- 작성자 프로필 -->
      <v-card-title class="text-body-1" id="writer">
        <v-list-item-avatar>
          <v-img v-if="room.profileImage" :src="room.profileImage" />
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
import ImageView from "@/components/module/ImageView";

export default {
  name: "LiveList",
  components: {
    ImageView,
  },
  data() {
    return {
      dialog: false,
      notifications: false,
      sound: true,
      widgets: false,
      room_name: "",
      publisher: "",
      hashtagList: [],
      fixedComment: "",
      imageReady: false,
    };
  },
  created() {
    const _this = this;
    //뭘입을까 참고
    this.findAllRoom()
      .then(() => {
        this.getChatRooms.forEach((room) => {
          _this.getImageList({ prefix: "live/" + room.roomId }).then((res) => {
            _this.$set(room, "imageUrls", res);
          });
        });
        this.getProfiles(this.getChatRooms);
      })
      .then(() => (this.imageReady = true));
    console.log("images", this.getChatRooms);
  },
  computed: {
    ...mapGetters(["getChatRooms"]),
  },
  methods: {
    ...mapActions(["findAllRoom", "getImageList", "getProfiles"]),
    ...mapMutations(["setChatRoomDetail", "setChatImageFiles"]),

    enterRoom(roomInfo) {
      console.log(roomInfo);
      this.setChatRoomDetail(roomInfo);
      this.$router.push("/live/detail");
    },
  },
};
</script>

<style scoped>
</style>
