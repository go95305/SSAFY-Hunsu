<template>
<!-- 라이브 채팅창 (아직 구현안돼서 댓글로 대체해놓음)-->
  <div>
    <v-container fluid>      
      <v-row>
        <v-col cols="12" sm="6" id="comment_input">
          <v-text-field
            label="바르고 고운 채팅:)"
            outlined
            rows="3"
            row-height="25"
          ></v-text-field>
        </v-col>
      </v-row>
    </v-container>

    <!--클릭할때만 하트애니메이션 작동되도록-->
    <!-- <v-icon >mdi-heart</v-icon>
    <p class="a">🧡</p>
    <p class="a">🧡</p>
    <p class="a">🧡</p>
    <p class="a">🧡</p>
    <p class="a">🧡</p> -->
  </div>

</template>

<script>
export default {
  name: "LiveDetailChat",
  data: () => ({
  }),

}
</script>

<style>
.a {
  position: absolute;
  bottom: -60px;
  width: 50px;
  height: 50px;
}

.a:first-of-type {
  left: 10px;
  animation: bubble 2s 1s linear infinite;
}

.a:nth-of-type(2) {
  left: 10px;
  animation: bubble 3s 1s linear infinite;
}

.a:nth-of-type(3) {
  left: 10px;
  animation: bubble 3.5s 1s linear infinite; 
}

.a:nth-of-type(4) {
  left: 10px;
  animation: bubble 3.1s 1s linear infinite;
}

.a:nth-of-type(5) {
  left: 10px;
  animation: bubble 3s 1s linear infinite;
}


@keyframes bubble {
  0% {
    bottom: -100px;
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
  to {
    bottom: 100%;
    opacity: 0;
  }
}
</style>