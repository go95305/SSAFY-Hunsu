<template>
<!-- OOTD 검색 버튼 -->
<div class="d-inline-block">
  <!-- 검색어 입력 -->
  <v-text-field v-model="searchWord" label="해시태그 OR 단어 검색" class="d-inline-block pl-3" width="100" @keydown.enter="getSearchedListInApi(searchWord)"></v-text-field>
  <!-- 검색어 입력 후 클릭하는 버튼 -->
  <v-btn icon class="d-inline-block" @click="getSearchedListInApi(searchWord)">
    <v-icon>mdi-magnify</v-icon>
  </v-btn>
  
</div>
</template>

<script>
import { mapActions } from "vuex";

export default {
  name: "OotdSearchBtn",
  data() {
    return {
      searchWord: ''
    }
  },
  methods: {
    ...mapActions(["getSearchedListInApi"]),
  }
}
</script>

<style>

</style>