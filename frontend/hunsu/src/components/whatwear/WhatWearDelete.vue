<template>
  <v-row justify="center">
    <v-dialog
      v-model="dialog"
      persistent
      max-width="290"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          color="primary"
          dark
          v-bind="attrs"
          v-on="on"
        >
          삭제
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="headline">
          삭제하기
        </v-card-title>
        <v-card-text>게시글을 삭제하시겠어요?</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="blue darken-4"
            text
            @click="dialog = false"
          >
            취소
          </v-btn>
          <v-btn
            color="red accent-4"
            text
            @click="dialog = false"
          >
            삭제하기
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
export default {
  name: "WhatWearDelete",
  data() {
    return {
      dialog: false,
    }
  }
}
</script>

<style>

</style>