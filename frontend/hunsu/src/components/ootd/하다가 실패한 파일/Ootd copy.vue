<template>
  <div>
    <h1>#OOTD</h1>
    <OotdIList :cards="cards" @select-card="onCardSelect"/>
  </div>
</template>

<script>
import OotdIList from '@/components/ootd/OotdIList'

export default {
  name: "Ootd",
  components: {
    OotdIList
  },
  data: function () {
    return {
      selectedCard: '',
      cards: [
      { title: 'Pre-fab homes', src: 'https://cdn.vuetifyjs.com/images/cards/house.jpg', flex: 6 },
      { title: 'Favorite road trips', src: 'https://cdn.vuetifyjs.com/images/cards/road.jpg', flex: 6 },
      { title: 'Best airlines', src: 'https://cdn.vuetifyjs.com/images/cards/plane.jpg', flex: 6 },
    ],
    }
  },
  methods: {
    onCardSelect: function (card) {
      this.selectedCard = card
    }
  }
}
</script>

<style>

</style>