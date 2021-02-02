<template>
  <!-- OOTD 디테일 -->
  <v-card elevation="24" max-width="450" class="mx-auto">
    <v-list one-line>
      <v-list-item style="height: 15px">
        <!-- 작성자 정보 -->
        <v-list-item-avatar>
          <v-img src="https://cdn.vuetifyjs.com/images/john.png"></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
<<<<<<< HEAD
          <v-list-item-title class="d-inline-block">{{ootdInfo.nickname}}</v-list-item-title>
        </v-list-item-content>

      <v-menu
        bottom
      >
        <template v-slot:activator="{ on, attrs }">
          <v-avatar
          v-bind="attrs"
          v-on="on"
=======
          <v-list-item-title class="d-inline-block"
            >John Leider</v-list-item-title
>>>>>>> fda25cb19ef3760b99bc659c8954c55e7c369bc6
          >
        </v-list-item-content>

        <v-menu bottom>
          <template v-slot:activator="{ on, attrs }">
            <v-avatar v-bind="attrs" v-on="on">
              <v-btn color="black" icon class="d-inline-block">
                <v-icon>mdi-dots-vertical</v-icon>
              </v-btn>
            </v-avatar>
          </template>

          <v-list>
            <!-- <v-list-item> -->
            <v-list-item
              v-for="(item, i) in items"
              :key="i"
              @click="goToPage(item)"
            >
              <v-list-item-title>{{ item.text }}</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </v-list-item>
    </v-list>
    <!-- OOTD 이미지들 -->
    <v-carousel
      :continuous="false"
      :cycle="cycle"
      :show-arrows="false"
      hide-delimiter-background
      delimiter-icon="mdi-minus"
      height="330"
    >
      <v-carousel-item v-for="(slide, i) in slides" :key="i">
        <v-sheet :color="colors[i]" height="100%" tile>
          <v-row class="fill-height" align="center" justify="center">
            <div class="display-3">{{ slide }} Slide</div>
          </v-row>
        </v-sheet>
      </v-carousel-item>
    </v-carousel>
    <v-list two-line>
      <v-list-item>
        <!-- 글 내용 -->
        <v-list-item-content>
          <v-list-item-title>{{ootdInfo.content}}</v-list-item-title>
          <v-list-item-subtitle>#ootd #ootd #ootd</v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn icon>
            <v-icon>mdi-heart</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </v-list>
    <DetailComment />
    <OotdList />
  </v-card>
</template>

<script>
<<<<<<< HEAD
import DetailComment from '@/components/DetailComment'
import OotdList from '@/components/ootd/OotdList'
import axios from "axios";


  export default {
    name: "OotdDetail",
    components: {
      DetailComment,
      OotdList
    },
    data () {
      return {
        ootdInfo: {},
        items: [
=======
import DetailComment from "@/components/DetailComment";
import OotdList from "@/components/ootd/OotdList";

export default {
  name: "OotdDetail",
  props: {
    ootdIdx: {
      type: Number,
    },
  },
  components: {
    DetailComment,
    OotdList,
  },
  data() {
    return {
      items: [
>>>>>>> fda25cb19ef3760b99bc659c8954c55e7c369bc6
        {
          text: "수정",
        },
        {
          text: "삭제",
        },
      ],
<<<<<<< HEAD
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
    created() {
      this.getOotdDetail()
    },
    methods: {
      goToPage(item) {
        // console.log(item.text)
        if (item.text === 'MyPage') {
          this.$router.push('/mypage')
        }
      },
      goToLogin() {
        this.$router.push('/login')
      },
      getOotdDetail() {
        const ootdIdx = this.$route.params.no
        axios.get(`http://i4c102.p.ssafy.io:8080/api/ootd/detail/${ootdIdx}`)
          .then(res => {
            this.ootdInfo = res.data
          })
          .catch(err => {
            console.error(err)
          })
      }
  }
  }
=======
      colors: [
        "green",
        "secondary",
        "yellow darken-4",
        "red lighten-2",
        "orange darken-1",
      ],
      cycle: false,
      slides: ["First", "Second", "Third", "Fourth", "Fifth"],
    };
  },
  methods: {
    goToPage(item) {
      // console.log(item.text)
      if (item.text === "MyPage") {
        this.$router.push("/mypage");
      }
    },
    goToLogin() {
      this.$router.push("/login");
    },
  },
};
>>>>>>> fda25cb19ef3760b99bc659c8954c55e7c369bc6
</script>

<style>
</style>