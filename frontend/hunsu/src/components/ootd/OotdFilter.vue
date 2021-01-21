<template>
  <v-switch
    v-model="cycle"
    label="정렬"
    inset
    class="d-inline-block"
  ></v-switch>
</template>

<script>
export default {
  name: "OotdFilter",
  data() {
    return {
      cycle: false,
    }
  }
}
</script>

<style>

</style>