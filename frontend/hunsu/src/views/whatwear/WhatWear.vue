<template>
  <div>
    <WhatWearWrite />
    <WhatWearList />
  </div>
</template>

<script>
import WhatWearList from "@/components/whatwear/WhatWearList"
import WhatWearWrite from "@/components/whatwear/WhatWearWrite"

export default {
  name: "WhatWear",
  components: {
    WhatWearList,
    WhatWearWrite,
  }
}
</script>

<style>


</style>