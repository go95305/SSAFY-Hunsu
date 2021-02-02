<template>
    <v-card
    max-width="450"
    class="mx-auto mt-1"
  >
    <WhatWearDetailCard 
      :nickname="this.nickname"
      :wear_idx="this.wear_idx" />
    <WhatWearUpdate />
    <WhatWearChart 
      :nickname="this.nickname"
      :wear_idx="this.wear_idx" />
    <DetailComment />
  </v-card>
</template>


<script>
import WhatWearChart from "@/components/whatwear/WhatWearChart"
import DetailComment from "@/components/DetailComment"
import WhatWearUpdate from "@/components/whatwear/WhatWearUpdate"
import WhatWearDetailCard from "@/components/whatwear/WhatWearDetailCard"

export default {
  name: "WhatWearDetail",
  components: {
    WhatWearChart,
    DetailComment,
    WhatWearUpdate,
    WhatWearDetailCard,
  },
  props: {
    nickname: {
      type: String,
    },
    wear_idx: {
      type: Number
    }
  },
}

</script>

<style>

</style>