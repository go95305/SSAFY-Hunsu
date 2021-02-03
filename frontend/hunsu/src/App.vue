<template>
  <v-app id="app">
    <MainNavBar />
    <v-app id="content"> <!--margin-top 걸려있음(네브바가 스크롤기능이 되면서 화면이 위로 올라가는걸 방지하기위해)-->
      <router-view />
    </v-app>
    <!-- <MainBottomBar /> bottom navigationbar -->
    <MainFooter />
  </v-app>
</template>

<script>
import MainFooter from '@/components/layout/MainFooter'
import MainNavBar from '@/components/layout/MainNavBar'
// import MainBottomBar from '@/components/layout/MainBottomBar'

export default {
  name: 'App',
  components: {
    MainFooter,
    MainNavBar,
    // MainBottomBar
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