<template>
<!-- 라이브 디테일 -->
  <v-card>
      <v-list two-line>
      <!-- 뒤로가기 버튼 (라이브 메인페이지로 이동) -->
      <div class="d-flex justify-space-between">
      <div class="d-inline-block">
      <v-btn
        icon
        @click="goToLiveMain"
      >
        <v-icon>mdi-chevron-left</v-icon>
      </v-btn>
      </div>
        <!-- 라이브 제목 -->
        <div
          class="d-inline-block text-truncate"
          style="max-width: 270px; height: 36px"
        >
          소개팅가요 도와주세요 !!!소개팅가요 도와주세요 !!!소개팅가요 도와주세요 !!!
        </div>
        <div class="d-inline-block">
        <v-menu
        bottom
      >
        <template v-slot:activator="{ on, attrs }">
          <v-avatar
          v-bind="attrs"
          v-on="on"
          >
            <v-btn
              color="black"
              icon
              class="d-inline-block"
            >
              <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>
          </v-avatar>
        </template>

        <v-list>
          <v-list-item
            v-for="(item, i) in items"
            :key="i"
            @click="goToPage(item)"
          >
            <v-list-item-title>{{ item.text }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
      </div>
      </div>
        <v-list-item style="height: 50px;">
        <v-list-item-avatar>
          <v-img src="https://cdn.vuetifyjs.com/images/john.png"></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
          <v-list-item-subtitle>작성자</v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action>
          123명 접속중
        </v-list-item-action>
      </v-list-item>
    </v-list>
      <v-img
        src="https://picsum.photos/350/165?random"
        max-height="125"
        contain
        class="grey darken-4"
      ></v-img>
    <LiveDetailChat />
    </v-card>
</template>

<script>
import LiveDetailChat from '@/components/live/LiveDetailChat'
  export default {
    components: {
      LiveDetailChat
    },
    data () {
      return {
        items: [
        {
          text: '수정'
        },
        {
          text: '삭제'
        }
      ],
        colors: [
          'green',
          'secondary',
          'yellow darken-4',
          'red lighten-2',
          'orange darken-1',
        ],
        cycle: false,
        slides: [
          'First',
          'Second',
          'Third',
          'Fourth',
          'Fifth',
        ],
      }
    },
    methods: {
      goToLiveMain() {
        this.$router.push('/live')
      },
      goToPage(item) {
      // console.log(item.text)
      if (item.text === 'MyPage') {
        this.$router.push('/mypage')
      }
      },
      goToLogin() {
        this.$router.push('/login')
      },
    }
  }
</script>

<style>

</style>