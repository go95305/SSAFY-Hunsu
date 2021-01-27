<template>
  <div>
    <v-app-bar
      color="white"
      dense
      height="80"
    >
    <v-toolbar-title>
      <v-img src="@/assets/hunsulogo.png" width="120">

      </v-img>
    </v-toolbar-title>


      <v-spacer></v-spacer>


      <v-btn @click="goToLogin()">로그인</v-btn>
      <v-menu
        left
        bottom
      >
        <template v-slot:activator="{ on, attrs }">
          <v-avatar
          v-bind="attrs"
          v-on="on"
          >
            <img
              src="https://cdn.vuetifyjs.com/images/john.jpg"
              alt="John"
            >
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
    </v-app-bar>
    <v-tabs
    fixed-tabs
    background-color="white"
    height="38"
    color="dark"
    >
    <v-tab :to="{ name: 'Home' }" class="px-2" >
      홈
    </v-tab>
    <v-tab :to="{ name: 'Live' }" class="px-2">
      실시간
    </v-tab>
    <v-tab :to="{ name: 'Ootd' }" class="px-2">
      #OOTD
    </v-tab>
    <v-tab :to="{ name: 'WhatWear' }" class="px-2">
      뭘 입을까
    </v-tab>
  </v-tabs>
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