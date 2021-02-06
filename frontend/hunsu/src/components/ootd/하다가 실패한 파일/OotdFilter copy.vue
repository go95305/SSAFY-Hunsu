<template>
<div>
<!-- 정렬 버튼 -->
      <v-btn-toggle
        tile
        color="red accent-3"
        group
      >
        <v-btn value="left" @click="getOotdListInApi()">
          최신순
        </v-btn>

        <v-btn value="center" @click="getPopularOotdListInApi()">
          인기순
        </v-btn>

      </v-btn-toggle>
</div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";

export default {
  name: "OotdFilter",
  computed: {...mapGetters(["getOotdList"])},
  methods: {
    ...mapActions(["getPopularOotdListInApi", "getOotdListInApi"]),
  }
}
</script>

<style>

</style>