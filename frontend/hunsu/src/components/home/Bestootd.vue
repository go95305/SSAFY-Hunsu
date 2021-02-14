<template>
<!-- 패션왕 -->
  <v-card class="mx-auto" color="white" flat> 
    <v-img src="@/assets/ootdtest3.png"
    height="50%"
    >
    </v-img>
    <v-card-title class="font-weight-regular">Best Dresser</v-card-title>
    <v-card-subtitle class="black--text pa-0 ml-4 font-weight-regular">#OOTD에서 Best Dresser에 도전하세요</v-card-subtitle>
    <v-card-subtitle class=" pa-0 ml-4 mt-3" @click="linkOotd()">GO TO #OOTD <v-icon>mdi-menu-right</v-icon></v-card-subtitle>
    
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