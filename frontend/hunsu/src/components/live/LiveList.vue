<template>
  <!-- 라이브 메인페이지 -->
  <div>
    <v-card
      v-for="(room, idx) in chatRooms"
      :key="idx"
      @click="enterRoom(room)"
    >
      <!-- 라이브 메인 사진 -->
      <!-- <ImageView :images="room.imageUrls" /> -->
      <ImageView :images="room.imageUrls" />

      <v-list two-line>
        <v-list-item>
          <!-- 작성자 프로필 -->
          <v-list-item-avatar>
            <v-img v-if="room.profileImage" :src="room.profileImage" />
            <v-img v-else src="@/assets/profilephoto.png" />
          </v-list-item-avatar>
          <v-list-item-content>
            <!-- 라이브 제목 -->
            <v-list-item-title>{{ room.title }}</v-list-item-title>
            <!-- 해시태그 -->
            <v-list-item-subtitle>
              <!-- 추후 해쉬태그에 검색 링크 걸 예정 -->
              <p
                v-for="(hashtag, i) in room.hashtagList"
                :key="i"
                style="display: inline"
              >
                {{ "#" + hashtag }}
              </p>
            </v-list-item-subtitle>
          </v-list-item-content>
          <v-list-item-action>
            <!-- 작성자, 접속자수 -->
            <div class="d-flex align-center">
              <v-icon color="red">mdi-circle-medium</v-icon>
              <v-list-item-subtitle
                >참여자수 : {{ room.userCount + 1 }}</v-list-item-subtitle
              >
            </div>
          </v-list-item-action>
        </v-list-item>
      </v-list>
    </v-card>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapMutations } from "vuex";
import ImageView from "@/components/module/ImageView";
import { EventBus } from "@/services/eventBus";

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
      chatRooms: {},
    };
  },
  created() {
    EventBus.$on("LiveWrite", async (room) => {
      const images = await this.getImageList({
        prefix: "live/" + room.roomId,
      });
      this.$set(room, "imageUrls", images);
      await this.getProfiles(room);
      this.enterRoom(room);
    });
  },
  async mounted() {
    // const _this = this;
    //뭘입을까 참고
    await this.findAllRoom();
    this.chatRooms = this.getChatRooms;
    this.chatRooms.forEach(async (room) => {
      const images = await this.getImageList({
        prefix: "live/" + room.roomId,
      });
      this.$set(room, "imageUrls", images);
    });
    await this.getProfiles(this.chatRooms);
  },
  computed: {
    ...mapGetters(["getChatRooms"]),
  },
  methods: {
    ...mapActions(["findAllRoom", "getImageList", "getProfiles"]),
    ...mapMutations(["setChatRoomDetail", "setChatImageFiles"]),

    enterRoom(roomInfo) {
      this.setChatRoomDetail(roomInfo);
      this.$router.push("/live/detail");
    },
  },
};
</script>

<style scoped>
</style>
