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
      <!-- <v-btn :to="{ name: 'Movie' }" text color="black">
        <span>실채훈</span>
      </v-btn>
      <v-btn :to="{ name: 'Movie' }" text color="black">
        <span>#OOTD</span>
      </v-btn>
      <v-btn>
        <span>뭘입을까</span>
      </v-btn> -->


      <v-spacer></v-spacer>

      <v-menu
        left
        bottom
      >
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            icon
            v-bind="attrs"
            v-on="on"
          >
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>

        <v-list>
          <v-list-item
            v-for="n in 5"
            :key="n"
            @click="() => {}"
          >
            <v-list-item-title>Option {{ n }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-app-bar>
  </div>
</template>

<script>
export default {
  name: "MainNavBar",
}
</script>

<style>

</style>