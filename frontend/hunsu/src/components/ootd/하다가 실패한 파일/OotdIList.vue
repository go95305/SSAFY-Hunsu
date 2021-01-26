<template>
  <v-container fluid>
<<<<<<< HEAD:frontend/hunsu/src/components/ootd/하다가 실패한 파일/OotdIList.vue
  <router-view></router-view>
=======
>>>>>>> e64ad85b678ed0432ab5f5df924d9ccddd5f7dff:frontend/hunsu/src/components/ootd/OotdIList.vue
      <v-row dense>
        <v-col
          v-for="card in cards"
          :key="card.title"
          :cols="card.flex"
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