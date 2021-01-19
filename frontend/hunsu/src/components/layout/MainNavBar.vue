<template>
<div id="app">
  <v-app id="inspire">
    <v-card class="overflow-hidden">
      <v-app-bar
        absolute
        color="#6A76AB"
        dark
        shrink-on-scroll
        prominent
        src="https://picsum.photos/1920/1080?random"
        fade-img-on-scroll
        scroll-target="#scrolling-techniques-3"
      >
        <template v-slot:img="{ props }">
          <v-img
            v-bind="props"
            gradient="to top right, rgba(100,115,201,.7), rgba(25,32,72,.7)"
          ></v-img>
        </template>
  
        <v-app-bar-nav-icon></v-app-bar-nav-icon>
  
        <v-toolbar-title>Hunsu</v-toolbar-title>
  
        <v-spacer></v-spacer>
  
        <v-btn icon>
          <v-icon>mdi-magnify</v-icon>
        </v-btn>
  
        <v-btn icon>
          <v-icon>mdi-heart</v-icon>
        </v-btn>
  
        <v-btn icon>
          <v-icon>mdi-dots-vertical</v-icon>
        </v-btn>
  
        <template v-slot:extension>
          <v-tabs align-with-title>
            <v-tab>홈</v-tab>
            <v-tab>실시간</v-tab>
            <v-tab>#OOTD</v-tab>
            <v-tab>뭘 입을까?</v-tab>
          </v-tabs>
        </template>
      </v-app-bar>
      <v-sheet
        id="scrolling-techniques-3"
        class="overflow-y-auto"
      >
        <v-container style="height: 1000px;"></v-container>
      </v-sheet>
    </v-card>
  </v-app>
</div>
</template>

<script>
export default {
  name: "MainNavBar",
}
</script>

<style>

</style>