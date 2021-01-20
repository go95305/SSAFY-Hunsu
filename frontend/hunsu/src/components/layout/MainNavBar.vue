<template>
  <div>
    <v-app-bar
      color="grey lighten-5"
      dense
    >
      <v-toolbar-title>Hunsu</v-toolbar-title>

      
      <v-btn :to="{ name: 'Home' }" text color="black">
        <span>홈</span>
      </v-btn>
      <v-btn :to="{ name: 'LiveChat' }" text color="black">
        <span>실채훈</span>
      </v-btn>
      <v-btn :to="{ name: 'Ootd' }" text color="black">
        <span>#OOTD</span>
      </v-btn>
      <v-btn :to="{ name: 'WhatWear' }" text color="black">
        <span>뭘입을까</span>
      </v-btn>


      <v-spacer></v-spacer>


      <v-btn @click="goToLogin()">로그인</v-btn>
      <v-menu
        left
        bottom
      >
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            v-bind="attrs"
            v-on="on">
            유저사진
          </v-btn>
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
    </v-app-bar>
  </div>
</template>

<script>
export default {
  name: "MainNavBar",
  data() {
    return {
      items: [
        {
          text: 'MyPage'
        },
        {
          text: 'Logout'
        }
      ]
    }
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
    }
  }
}
</script>

<style>

</style>