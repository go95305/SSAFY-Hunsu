<template>
    <v-card
    max-width="450"
    class="mx-auto mt-1"
  >
    <WhatWearDetailCard />
    <WhatWearChart />
    <DetailComment />
  </v-card>

</template>

<script>
import WhatWearDetailCard from "@/components/whatwear/WhatWearDetailCard"
import WhatWearChart from "@/components/whatwear/WhatWearChart"
import DetailComment from "@/components/DetailComment"

export default {
  name: "WhatWearDetail",
  components: {
    WhatWearDetailCard,
    WhatWearChart,
    DetailComment
  }
}
</script>

<style>

</style>