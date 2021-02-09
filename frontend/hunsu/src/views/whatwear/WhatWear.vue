<template>
  <div>
    <WhatWearCard />
    <WhatWearList />
    <WhatWearWrite />
  </div>
</template>

<script>
import WhatWearList from "@/components/whatwear/WhatWearList"
import WhatWearWrite from "@/components/whatwear/WhatWearWrite"
import WhatWearCard from "@/components/whatwear/WhatWearCard"


export default {
  name: "WhatWear",
  components: {
    WhatWearList,
    WhatWearWrite,
    WhatWearCard,
  }
}
</script>

<style>


</style>