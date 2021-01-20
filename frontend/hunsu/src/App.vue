<template>
  <v-app id="app">
    <MainNavBar />
    <v-app id="content">
      <router-view />
    </v-app>
    <MainFooter />
  </v-app>
</template>

<script>
import MainFooter from '@/components/layout/MainFooter'
import MainNavBar from '@/components/layout/MainNavBar'

export default {
  name: 'App',
  components: {
    MainFooter,
    MainNavBar
  },

  data: () => ({
    //
  }),
};
</script>
