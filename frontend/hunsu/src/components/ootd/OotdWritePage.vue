<template>
  <v-row justify="center" class="d-inline-block">
    <v-dialog
      v-model="dialog"
      fullscreen
      hide-overlay
      transition="dialog-bottom-transition"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          color="primary"
          dark
          small
          fab
          v-bind="attrs"
          v-on="on"
        >
          <v-icon>mdi-pencil</v-icon>
        </v-btn>
      </template>
      <v-card>
        <v-toolbar
          dark
          color="black"
        >
          <v-btn
            icon
            dark
            @click="dialog = false"
          >
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>OOTD 작성</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <v-btn
              dark
              text
              @click="dialog = false"
            >
              Save
            </v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <v-list
          three-line
          subheader
        >
          <v-subheader>사진 등록</v-subheader>
          <v-img
            src="https://picsum.photos/200/300?image=20"
            lazy-src="https://picsum.photos/10/6?image=20"
            aspect-ratio="1"
            class="grey lighten-2"
          >
          </v-img>
          <v-file-input
            label="File input"
            filled
            prepend-icon="mdi-camera"
            width="100"
          ></v-file-input>

        </v-list>
        <v-divider></v-divider>
        <v-list
          three-line
          subheader
        >
          <v-subheader>OOTD 설명</v-subheader>
            <div>
              <v-text-field
                label="설명 추가"
                :rules="rules"
                hide-details="auto"
                class="px-5"
              ></v-text-field>
              <v-text-field label="해시태그 추가" class="px-5"></v-text-field>
            </div>
        </v-list>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
  export default {
    name: "OotdWritePage",
    data () {
      return {
        dialog: false,
        notifications: false,
        sound: true,
        widgets: false,
        rules: [
          value => !!value || 'Required.',
          value => (value && value.length >= 3) || 'Min 3 characters',
        ],
      }
    },
  }
</script>

<style>

</style>