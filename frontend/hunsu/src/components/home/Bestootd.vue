<template>
<!-- 패션왕 -->
  <v-card class="mx-auto" color="white" flat> 
    <v-card-title class="font-weight-regular blue-grey darken-4 white--text text-center pl-10">✨Best Dresser Of This Week✨</v-card-title>
    <v-img src="@/assets/bestpic.jpg"
    height="50%"
    >
    </v-img>
    <v-card-subtitle class="black--text pb-1 font-weight-regular">#OOTD에서 Best Dresser에 도전하세요</v-card-subtitle>
    <v-card-subtitle class=" pa-0 ml-4 pb-1" @click="linkOotd()">GO TO #OOTD <v-icon>mdi-menu-right</v-icon></v-card-subtitle>
    
  </v-card>

</template>

<script>
export default {
  name: "Bestootd",
  methods: {
    linkOotd() {
      this.$router.push("/ootd");
    }
  }
}
</script>

<style>

</style>