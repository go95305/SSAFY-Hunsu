<template>
  <v-card flat class="mt-3" >
    <div >
        <v-carousel 
          v-model="model" 
          id="home_carousel" 
          cycle 
          show-arrows-on-hover 
          hide-delimiter-background>
          <v-carousel-item>
          <v-img src="@/assets/home1.png" @click="goToAboutUS()">
          </v-img>
          </v-carousel-item>
          <v-carousel-item>
          <v-img src="@/assets/home2.png">
          </v-img>
          </v-carousel-item>
              <v-row
                class="fill-height"
                align="center"
                justify="center"
              >
              </v-row>
        </v-carousel>
      </div>
  </v-card>
</template>

<script>
export default {
  name: "AboutUsCard",
  methods: {
    goToAboutUS() {
      this.$router.push("/aboutus");
    }
  }
}
</script>

<style>
#home_carousel {
  height: 150px !important;
}
</style>