<template>
  <v-app id="app">
    <MainNavBar />
    <v-app id="content">
      <router-view />
    </v-app>
    <MainBottomBar />
    <MainFooter />
  </v-app>
</template>

<script>
import MainFooter from '@/components/layout/MainFooter'
import MainNavBar from '@/components/layout/MainNavBar'
import MainBottomBar from '@/components/layout/MainBottomBar'

export default {
  name: 'App',
  components: {
    MainFooter,
    MainNavBar,
    MainBottomBar
  },

  data: () => ({
    //
  }),
};
</script>

<style>
#content {
  background-color: #F5F5F5;
  margin-top: 100px;
}
</style>