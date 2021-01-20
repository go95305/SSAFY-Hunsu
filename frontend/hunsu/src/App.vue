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
import MainNavBar from './components/layout/MainNavBar';
import MainFooter from './components/layout/MainFooter';

export default {
  name: 'App',
  components: {
    MainNavBar,
    MainFooter,
  },

  data: () => ({
    //
  }),
};
</script>

<style>
#content {
  background-color: #F5F5F5;
}
</style>