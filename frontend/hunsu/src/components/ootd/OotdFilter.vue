<template>
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
</template>

<script>
export default {
  name: "OotdFilter"
}
</script>

<style>

</style>