<template>
    <v-card
    max-width="450"
    class="mx-auto mt-1"
  >
    <WhatWearUpdate />
    <WhatWearDetailCard />
    <WhatWearChart />
    <DetailComment />
  </v-card>

</template>

<script>
import WhatWearDetailCard from "@/components/whatwear/WhatWearDetailCard"
import WhatWearChart from "@/components/whatwear/WhatWearChart"
import DetailComment from "@/components/DetailComment"
import WhatWearUpdate from "@/components/whatwear/WhatWearUpdate"

export default {
  name: "WhatWearDetail",
  components: {
    WhatWearDetailCard,
    WhatWearChart,
    DetailComment,
    WhatWearUpdate,
  }
}
</script>

<style>

</style>