<template>
  <v-container fluid>
      <v-row dense>
        <v-col>
          <OotdListItem 
            v-for="card in cards"
            :key="card.title"
            :cols="card.flex"
            :card="card"
            @select-card="onCardSelect"
          />
        
        </v-col>
      </v-row>
    </v-container>
</template>

<script>
import OotdListItem from '@/components/ootd/OotdListItem'

export default {
  name: "OotdIList",
  components: {
    OotdListItem,
  },
  props: {
    cards: Array,
  },
  methods: {
    onSelectCard: function (card) {
      this.$$emit('select-card', card)
    }
  }
}
</script>


<style>

</style>