<template>
  <v-list three-line subheader>
    <v-subheader>사진 등록</v-subheader>

    <!-- 이미지 업로드 -->
    <input
      ref="imageInput"
      type="file"
      multiple
      hidden
      @change="onChangeImages"
    />
    <div class="display: inline-block;" v-if="getUploadImageUrls.length === 0">
      <v-btn class="mx-5" icon x-large @click="onClickImageUpload"
        ><v-icon>mdi-camera</v-icon></v-btn
      >
    </div>

    <!-- <v-sheet class="mx-auto" elevation="3" max-width="300"> -->
    <!-- 현재는 사진 업로드만, 올렸던 사진 삭제는 미구현상태 -->
    <div v-else>
      <v-slide-group
        v-model="getUploadImageUrls"
        class="pa-4"
        center-active
        show-arrows
      >
        <v-slide-item v-for="(imageUrl, idx) in getUploadImageUrls" :key="idx">
          <v-img class="mx-5 my-5" :src="imageUrl" height="100" width="100">
          </v-img>
        </v-slide-item>
        <div>
          <br />
          <v-btn
            class="mx-5 mt-5"
            icon
            @click="onClickImageUpload"
            x-large
            ><v-icon>mdi-camera</v-icon></v-btn
          >
          <input
            type="file"
            id="file"
            ref="imageInput"
            @change="onChangeImages"
            multiple
            hidden
          />
        </div>
      </v-slide-group>
    </div>
    <!-- </v-sheet> -->
  </v-list>
</template>

<script>
import { mapState, mapGetters, mapMutations } from "vuex";

export default {
  name: "ImageUpload",
  data() {
    return {
      ...mapState(["uploadImageUrls"]),
    };
  },
  computed: {
    ...mapGetters(["getUploadImageUrls", "getUploadImageFiles"]),
  },
  methods: {
    ...mapMutations(["setUploadImageUrls", "setUploadImageFiles"]),
    onClickImageUpload() {
      this.$refs.imageInput.click();
    },
    onChangeImages(e) {
      console.log(e.target.files);

      this.setUploadImageFiles(e.target.files);
      this.setUploadImageUrls();
      console.log("onChange imageURl ", this.getUploadImageUrls);
    },
  },
};
</script>

<style>
</style>