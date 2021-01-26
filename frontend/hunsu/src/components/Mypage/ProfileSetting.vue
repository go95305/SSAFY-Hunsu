<template>
  <v-dialog
    v-model="dialog"
    fullscreen
    hide-overlay
    transition="dialog-bottom-transition"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        icon
        v-bind="attrs"
        v-on="on">
        <v-icon>mdi-cog-outline</v-icon>
        </v-btn>
    </template>
    <v-card
      >
      <v-toolbar
        dark
        color="dark"
      >
        <v-btn
          icon
          dark
          @click="dialog = false"
        >
          <v-icon>mdi-close</v-icon>
        </v-btn>
        <v-toolbar-title>프로필수정</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-toolbar-items>
          <v-btn
            dark
            text
            @click="dialog = false"
          >
            완료
          </v-btn>
        </v-toolbar-items>
      </v-toolbar>
      <div id="profile_image">
      <v-avatar
        width="100px"
        height="100px">
        <img
          src="https://cdn.vuetifyjs.com/images/john.jpg"
          alt="John"
        >
      </v-avatar>
      <v-file-input
        truncate-length="15"
        hide-input
      ></v-file-input>
      </div>
      <div id="info_input">
      <v-text-field
        label="닉네임"
        value="닉네임"
        hint="닉네임 중복불가안내, 규칙안내"
        outlined
      ></v-text-field>
      <p class="text-h6 font-weight-bold">추가정보</p>
      <p class="text-subtitle2">추천서비스 제공에 쓰이는 정보입니다.</p>
      <v-text-field
        label="키"
        :value="height"
        hint="cm를 제외하고 적어주세요"
        outlined
      ></v-text-field>
      <v-select
        :items="items"
        label="체형"
        outlined
      ></v-select>
      </div>
    </v-card>
  </v-dialog>
</template>
<script>
export default {
  name: "ProfileSetting",
  data() {
    return {
      dialog: false,
      notifications: false,
      sound: true,
      widgets: false,
      height: "",
      items: ['XS', 'S', 'M', 'L', 'XL', 'XXL'],
    }
  }
}
</script>

<style>
#profile_image {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}
#info_input {
  margin: auto;
  width: 365px;
}

</style>