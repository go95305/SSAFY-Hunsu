<template>
  <v-card>
      <v-img
        src="https://picsum.photos/350/165?random"
        max-height="125"
        contain
        class="grey darken-4"
      ></v-img>
          <v-list two-line>
      <v-list-item>
        <v-list-item-avatar>
          <v-img src="https://cdn.vuetifyjs.com/images/john.png"></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
          <v-list-item-title>한시간 뒤에 소개팅가요 ㅠㅠ 도와주세요</v-list-item-title>
          <v-list-item-subtitle>고정댓글</v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action>
          123명 접속중
        </v-list-item-action>
      </v-list-item>
    </v-list>
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
  }
</script>

<style>

</style>