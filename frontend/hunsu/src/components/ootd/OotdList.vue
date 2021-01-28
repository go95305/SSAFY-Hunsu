<template>
<!-- OOTD 메인 페이지 -->
<div>
  <!-- OOTD 하나 클릭하면 디테일페이지 뜨는 섹션 -->
  <!-- <router-view></router-view> -->
  <v-card
    elevation="24"
    max-width="450"
    class="mx-auto"
    @click="goToOotdDetail"
  >
    <v-system-bar lights-out></v-system-bar>
    <!-- OOTD 사진 -->
    <v-carousel
      :continuous="false"
      :cycle="cycle"
      :show-arrows="false"
      hide-delimiter-background
      delimiter-icon="mdi-minus"
      height="330"
    >
      <v-carousel-item
        v-for="(slide, i) in slides"
        :key="i"
      >
        <v-sheet
          :color="colors[i]"
          height="100%"
          tile
        >
          <v-row
            class="fill-height"
            align="center"
            justify="center"
          >
            <div class="display-3">
              {{ slide }} Slide
            </div>
          </v-row>
        </v-sheet>
      </v-carousel-item>
    </v-carousel>
    <v-list two-line>
      <v-list-item>
        <!-- 작성자 프로필 -->
        <v-list-item-avatar>
          <v-img src="https://cdn.vuetifyjs.com/images/john.png"></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
          <v-list-item-title>John Leider</v-list-item-title>
          <v-list-item-subtitle>Author</v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action>
          <!-- 좋아요 버튼 -->
          <v-btn icon>
            <v-icon>mdi-heart</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </v-list>
  </v-card>
  </div>
</template>

<script>
  export default {
    name: "OotdList",
    data () {
          return {
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
      goToOotdDetail() {
        this.$router.push('/ootd/detail')
      }
    }
  }
</script>
