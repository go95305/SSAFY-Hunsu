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
          color="deep-purple accent-4"
          fixed-tabs
        >
          <v-tab>OOTD</v-tab>
          <v-tab >좋아요</v-tab>
          <v-tab>팔로워</v-tab>
          <v-tab>팔로잉</v-tab>

          <v-tab-item
            v-for="n in 3"
            :key="n"
          >

          </v-tab-item>
        </v-tabs>
      </v-card>
    </v-layout>
  </v-container>


</template>
<script>
export default {
  name: "Profile"
}
</script>

<style>

</style>