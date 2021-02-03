<template>
<!-- 네비게이션 바 고정 -->
  <div>
    <v-app-bar
      color="white"
      dense
      height="50"
      app
    >
    <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->
    <v-toolbar-title>
      <v-img src="@/assets/hunsulogo.png" width="80" id="logo">
      </v-img>


    </v-toolbar-title>


      <v-spacer></v-spacer>


        <v-icon class="mr-2" color="black" @click="goToLogin()">mdi-account-outline</v-icon>
        <v-icon color="black">mdi-bell-outline</v-icon>
      <v-menu
        left
        bottom
      >
        <!-- <template v-slot:activator="{ on, attrs }">
          <v-avatar
          v-bind="attrs"
          v-on="on"
          >
            <img
              src="https://cdn.vuetifyjs.com/images/john.jpg"
              alt="John"
            >
          </v-avatar>
        </template> -->

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
        <template v-slot:extension>
        <v-tabs
        fixed-tabs
        background-color="white"
        height="50"
        color="red accent-3"
        app
        >
        <v-tab :to="{ name: 'Home' }" class="px-2" style="color: black">
          홈
        </v-tab>
        <v-tab :to="{ name: 'Live' }" class="px-2" style="color: black">
          실시간
        </v-tab>
        <v-tab :to="{ name: 'Ootd' }" class="px-2" style="color: black">
          #OOTD
        </v-tab>
        <v-tab :to="{ name: 'WhatWear' }" class="px-2" style="color: black">
          뭘 입을까
        </v-tab>
      </v-tabs>
    </template>
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
#logo {
  margin: 0 auto
}
</style>