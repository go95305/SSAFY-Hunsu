<template>
  <v-footer
    dark
    padless
  >
    <v-card
      flat
      tile
      class="dark white--text text-center"
      style="width: 100%"
    >
      <v-card-text>
        <v-btn
          v-for="icon in icons"
          :key="icon"
          class="mx-4 white--text"
          icon
        >
          <v-icon size="24px">
            {{ icon }}
          </v-icon>
        </v-btn>
      </v-card-text>

      <v-card-text class="white--text pt-0">
        개발자 : 한재희, 고유창, 박홍균, 최진영, 이지현 ｜ 소속 : SSAFY 4기 광주 1반 ｜ 고객센터 : 000-0000
      </v-card-text>
      <v-card-text class="white--text pt-0">
        Copyright © 주식회사 Moviement All right reserved. 
      </v-card-text>
      
      <v-divider></v-divider>

      <v-card-text class="white--text">
        {{ new Date().getFullYear() }} — <strong>C102</strong>
      </v-card-text>
    </v-card>
  </v-footer>
</template>

<script>
export default {
  name: 'MainFooter',
  data() {
      return {
          icons: [
              'mdi-facebook',
              'mdi-twitter',
              'mdi-linkedin',
              'mdi-instagram',
          ]
      }
  }
}
</script>

<style>

</style>