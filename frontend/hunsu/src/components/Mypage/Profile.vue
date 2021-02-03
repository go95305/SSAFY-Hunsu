<template>
  <v-container>
    <v-layout justify-center>
      <v-card
      width="800"
      height="100%"
      >
        <div class="d-flex">
          <!--프로필이미지-->
          <!--웹 전체화면일때-->
          <v-avatar
          width="100"
          height="100"
          class="mt-10 ml-10 mb-8 hidden-sm-and-down">
            <img
              src="https://cdn.vuetifyjs.com/images/john.jpg"
              alt="John"
            >
          </v-avatar>
          <!--모바일-->
          <v-avatar
          width="80"
          height="65"
          class="mt-7 ml-7 hidden-sm-and-up"
          style="margin: 80px 60px 0px 20px">
          
            <img
              src="https://cdn.vuetifyjs.com/images/john.jpg"
              alt="John"
            >
          </v-avatar>
          <!--유저닉네임-->
          <!-- <p class="font-weight-black text-h5 hidden-sm-and-down" style="margin: 60px 20px">닉네임</p>
          <p class="font-weight-black subtitle-1 hidden-sm-and-up" style="margin: 40px 10px">닉네임</p> -->

          <!--팔로우버튼, 팔로우목록 dialog-->
          <v-dialog
            transition="dialog-top-transition"
            max-width="600"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                color="primary"
                v-bind="attrs"
                v-on="on"
                class="mt-16 ml-8 hidden-sm-and-down"
              >팔로우</v-btn>
              <v-btn
                color="primary"
                v-bind="attrs"
                v-on="on"
                class="mt-16 mr-2 hidden-sm-and-up"
              >팔로우</v-btn>
            </template>
            <template v-slot:default="dialog">
              <v-card>
                <v-toolbar
                  color="dark"
                  dark
                >팔로우</v-toolbar>
                <v-list subheader>
                    <v-list-item
                      v-for="chat in recent"
                      :key="chat.title"
                    >
                      <v-list-item-avatar>
                        <v-img
                          :alt="`${chat.title} avatar`"
                          :src="chat.avatar"
                        ></v-img>
                      </v-list-item-avatar>

                      <v-list-item-content>
                        <v-list-item-title v-text="chat.title"></v-list-item-title>
                      </v-list-item-content>
                    </v-list-item>
                  </v-list>
                <v-card-actions class="justify-end">
                  <v-btn
                    text
                    @click="dialog.value = false"
                  >Close</v-btn>
                </v-card-actions>
              </v-card>
            </template>
          </v-dialog>
          <!--팔로잉버튼, 팔로잉목록dialog-->
          <v-dialog
            transition="dialog-top-transition"
            max-width="600"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                color="primary"
                v-bind="attrs"
                v-on="on"
                class="mt-16 ml-5 hidden-sm-and-down"
              >팔로잉</v-btn>
              <v-btn
                color="primary"
                v-bind="attrs"
                v-on="on"
                class="mt-16 hidden-sm-and-up"
              >팔로잉</v-btn>
            </template>
            <template v-slot:default="dialog">
              <v-card>
                <v-toolbar
                  color="dark"
                  dark
                >팔로잉</v-toolbar>
                  <v-list subheader>
                      <v-list-item
                        v-for="chat in recent"
                        :key="chat.title"
                      >
                        <v-list-item-avatar>
                          <v-img
                            :alt="`${chat.title} avatar`"
                            :src="chat.avatar"
                          ></v-img>
                        </v-list-item-avatar>

                        <v-list-item-content>
                          <v-list-item-title v-text="chat.title"></v-list-item-title>
                        </v-list-item-content>
                      </v-list-item>
                    </v-list>
                <v-card-actions class="justify-end">
                  <v-btn
                    text
                    @click="dialog.value = false"
                  >Close</v-btn>
                </v-card-actions>
              </v-card>
            </template>
          </v-dialog>

          <!--설정아이콘-->
          <ProfileSetting />
        </div>
        <div>
          <!--유저닉네임-->
          <p class="font-weight-black text-h5 hidden-sm-and-down" style="margin: 10px 30px">닉네임</p>
          <p class="font-weight-black subtitle-1 hidden-sm-and-up" style="margin: 10px 30px">닉네임</p>
        </div>

        <v-tabs
          v-model="tab"
          background-color="white"
          color="blue"
          grow
        >
          <v-tab
            v-for="item in items"
            :key="item"
          >
            {{ item }}
          </v-tab>
        </v-tabs>
        <v-tabs-items v-model="tab">
          <v-tab-item
            v-for="item in items"
            :key="item"
          >
            <v-container class="white">
              <v-row no-gutters>
                <v-col
                  v-for="i in 9"
                  :key="i"
                  cols="4"
                >
                  <v-card
                    outlined
                    tile
                  >
                    <v-img
                    src="@/assets/ootdtest.png">
                    </v-img>
                  </v-card>
                </v-col>
              </v-row>
            </v-container>
          </v-tab-item>
        </v-tabs-items>
      </v-card>
    </v-layout>
  </v-container>


</template>
<script>
import axios from "axios";
import ProfileSetting from "@/components/Mypage/ProfileSetting"

export default {
  name: "Profile",
  components: {
    ProfileSetting,
  },
  data() {
    return {
      tab: null,
      items: [
        'OOTD', '좋아요',
      ],
      recent: [
        {
          active: true,
          avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
          title: 'Jason Oner',
        },
        {
          active: true,
          avatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg',
          title: 'Mike Carlson',
        },
        {
          avatar: 'https://cdn.vuetifyjs.com/images/lists/3.jpg',
          title: 'Cindy Baker',
        },
        {
          avatar: 'https://cdn.vuetifyjs.com/images/lists/4.jpg',
          title: 'Ali Connors',
        },
      ]
    }
  },
  created() {
    this.getProfile();
  },
  methods: {
    getProfile() {
      axios
        .get("http://i4c102.p.ssafy.io:8080/api/ootd/")
        .then(() => {
        })
        .catch((err) => {
          console.error(err);
        });
    },
  },
}
</script>

<style>

</style>