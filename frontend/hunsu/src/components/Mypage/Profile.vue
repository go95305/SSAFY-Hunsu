<template>
  <v-container>
    <v-layout justify-center>
      <v-card
      width="800"
      height="180">
      <div class="flex">
          <!--프로필이미지-->
          <v-avatar
          width="100"
          height="100"
          class="mt-10 ml-10 mb-8 hidden-sm-and-down">
            <img
              src="https://cdn.vuetifyjs.com/images/john.jpg"
              alt="John"
            >
          </v-avatar>
          <v-avatar
          width="80"
          height="80"
          class="mt-7 ml-7 hidden-sm-and-up">
            <img
              src="https://cdn.vuetifyjs.com/images/john.jpg"
              alt="John"
            >
          </v-avatar>
          <!--설정아이콘-->
          <v-icon>mdi-cog-outline</v-icon>
      </div>
      <v-tabs
        v-model="tab"
        background-color="white"
        color="blue"
        grow
      >
        <v-tab
          v-for="item in items"
          :key="item"
        >
          {{ item }}
        </v-tab>
      </v-tabs>
        <v-tabs-items v-model="tab">
          <v-tab-item
            v-for="item in items"
            :key="item"
          >
            <v-container class="white">
              <v-row no-gutters>
                <v-col
                  v-for="i in 6"
                  :key="i"
                  cols="12"
                  sm="4"
                >
                  <v-card
                    class="pa-2"
                    outlined
                    tile
                  >
                    <v-img
                    src="@/assets/ootdtest.png">
                    </v-img>
                  </v-card>
                </v-col>
              </v-row>
            </v-container>
          </v-tab-item>
        </v-tabs-items>
      </v-card>
    </v-layout>
  </v-container>


</template>
<script>
export default {
  name: "Profile",
  data() {
    return {
      tab: null,
      items: [
        'OOTD', '좋아요', '팔로워', '팔로잉',
      ],
    }
  }
}
</script>

<style>

</style>