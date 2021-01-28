<template>
<!-- 정렬 버튼 -->
      <v-btn-toggle
        v-model="text"
        tile
        color="red accent-3"
        group
      >
        <v-btn value="left">
          최신순
        </v-btn>

        <v-btn value="center">
          인기순
        </v-btn>
      </v-btn-toggle>
</template>

<script>
export default {
  name: "OotdFilter",
  data() {
    return {
      cycle: false,
    }
  }
}
</script>

<style>

</style>