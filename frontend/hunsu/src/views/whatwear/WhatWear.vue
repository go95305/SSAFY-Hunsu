<template>
  <v-container
  id="whatwear"> 
    <WhatWearWrite />
<<<<<<< HEAD
    <WhatWearList />
  </v-container>
=======
    <WhatWearList style="margin: 20px" />
  </div>
>>>>>>> 493447e4a67de6e18fd74d4e78bc257f2d265b3d
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