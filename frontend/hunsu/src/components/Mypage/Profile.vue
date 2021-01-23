<template>
<v-container>
  <v-layout justify-center>
    <v-card
    width="800"
    height="650">
    <div class="flex">
        <!--프로필이미지-->
        <v-avatar
        width="100"
        height="100"
        class="mt-10 ml-10">
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
        centered
      >
        <v-tab>OOTD</v-tab>
        <v-tab>좋아요</v-tab>
        <v-tab>팔로워</v-tab>
        <v-tab>팔로잉</v-tab>

        <v-tab-item
          v-for="n in 3"
          :key="n"
        >
          <v-container fluid>
            <v-row>
              <v-col
                v-for="i in 6"
                :key="i"
                cols="12"
                md="4"
              >
                <v-img
                  :src="`https://picsum.photos/500/300?image=${i * n * 5 + 10}`"
                  :lazy-src="`https://picsum.photos/10/6?image=${i * n * 5 + 10}`"
                  aspect-ratio="1"
                ></v-img>
              </v-col>
            </v-row>
          </v-container>
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