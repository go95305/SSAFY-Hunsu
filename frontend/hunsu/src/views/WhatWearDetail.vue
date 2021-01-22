<template>
  <div>
    <h1>뭘입을까디테일</h1>
    <WhatWearDetailCard />
    <WhatWearChart />
    <WhatWearCommentCreate />
    <WhatWearCommentList />
  </div>
</template>

<script>
import WhatWearDetailCard from "@/components/whatwear/WhatWearDetailCard"
import WhatWearChart from "@/components/whatwear/WhatWearChart"
import WhatWearCommentList from "@/components/whatwear/WhatWearCommentList"
import WhatWearCommentCreate from "@/components/whatwear/WhatWearCommentCreate"

export default {
  name: "WhatWearDetail",
  components: {
    WhatWearDetailCard,
    WhatWearChart,
    WhatWearCommentList,
    WhatWearCommentCreate,
  }
}
</script>

<style>

</style>