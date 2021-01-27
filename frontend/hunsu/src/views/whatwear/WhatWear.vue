<template>
  <v-container
  id="whatwear"> 
    <WhatWearWrite />
    <WhatWearList />
  </v-container>
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
@media screen and (min-width: 769px) {
  #whatwear {
    width: 40%;
  }
}
@media screen and (max-width: 768px) {
  #whatwear {
    width: 100%
  }
}

</style>