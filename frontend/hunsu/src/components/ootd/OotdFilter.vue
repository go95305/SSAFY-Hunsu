<template>
<<<<<<< HEAD

      <v-btn-toggle
        v-model="text"
        tile
        color="deep-purple accent-3"
        group
      >
        <v-btn value="left">
          최신순
        </v-btn>

        <v-btn value="center">
          인기순
        </v-btn>
      </v-btn-toggle>
=======
  <v-menu transition="slide-y-transition">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        color="primary"
        class="ma-5"
        v-bind="attrs"
        v-on="on"
      >
        정렬
      </v-btn>
    </template>
    <v-list>
      <v-list-item
        v-for="n in 2"
        :key="n"
        link
      >
        <v-list-item-title v-text="'Item ' + n"></v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>
>>>>>>> 7f40a4edad481f6e4b56b106c52831a7ae870ae2
</template>

<script>
export default {
  name: "OotdFilter"
}
</script>

<style>

</style>