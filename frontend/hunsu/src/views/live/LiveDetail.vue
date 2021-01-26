<template>
  <v-card>
      <v-list two-line>
      <v-btn
      class="d-inline-block"
        icon
        @click="goToLiveMain"
      >
        <v-icon>mdi-chevron-left</v-icon>
      </v-btn>
        <span
          class="d-inline-block text-truncate"
          style="max-width: 300px;"
        >
          소개팅가요 도와주세요 !!!소개팅가요 도와주세요 !!!소개팅가요 도와주세요 !!!
        </span>
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
      }
    }
  }
</script>

<style>

</style>