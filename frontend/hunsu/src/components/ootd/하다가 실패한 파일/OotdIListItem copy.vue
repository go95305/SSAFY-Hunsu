<template>
  <v-card>
    <v-img
      @click="selectCard"
      :src="card.src"
      class="white--text align-end"
      gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
      height="200px"
    >
      <v-card-title v-text="card.title"></v-card-title>
    </v-img>

    <v-card-actions>
      <v-spacer></v-spacer>

      <v-btn text color="black">
        <span>작성자</span>
      </v-btn>
      <v-spacer></v-spacer>

      <v-btn icon>
        <v-icon>mdi-heart</v-icon>
      </v-btn>

    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  name: "OotdListItem",
  props: {
    card: Object,
  },
  methods: {
    selectCard: function() {
      this.$emit('select-card', this.card)
    }
  }
}
</script>

<style>

</style>