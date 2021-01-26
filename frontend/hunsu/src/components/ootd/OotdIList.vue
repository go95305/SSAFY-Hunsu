<template>
<<<<<<< HEAD
<div>
  <router-view></router-view>
  <v-card
    elevation="24"
    max-width="450"
    class="mx-auto"
    @click="goToOotdDetail"
  >
    <v-system-bar lights-out></v-system-bar>
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
=======
  <v-container fluid>
      <v-row dense>
        <v-col
          v-for="card in cards"
          :key="card.title"
          :cols="card.flex"
>>>>>>> 7f40a4edad481f6e4b56b106c52831a7ae870ae2
        >
          <v-card @click="goToOotdDetail">
            <v-img
              :src="card.src"
              class="white--text align-end"
              gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
              height="200px"
            >
              <v-card-title v-text="card.title"></v-card-title>
            </v-img>

            <v-card-actions>
              <v-spacer></v-spacer>

              <v-btn text color="black">
                <span>작성자</span>
              </v-btn>
              <v-spacer></v-spacer>
              <v-spacer></v-spacer>

              <v-btn icon>
                <v-icon>mdi-heart</v-icon>
              </v-btn>

            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
</template>

<script>
  export default {
    name: "OotdIList",
    data: () => ({
      cards: [
        { title: 'Pre-fab homes', src: 'https://cdn.vuetifyjs.com/images/cards/house.jpg', flex: 6 },
        { title: 'Favorite road trips', src: 'https://cdn.vuetifyjs.com/images/cards/road.jpg', flex: 6 },
        { title: 'Best airlines', src: 'https://cdn.vuetifyjs.com/images/cards/plane.jpg', flex: 6 },
      ],
    }),

    methods: {
      goToOotdDetail() {
        this.$router.push('/ootd/detail')
      }
    }
  }
</script>


<style>

</style>

