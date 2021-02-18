<template>
  <div style="flex">
    <LoginFilter />
    <WhatWearCard />
    <WhatWearList />
    <WhatWearWrite />
  </div>
</template>

<script>
import WhatWearList from "@/components/whatwear/WhatWearList";
import WhatWearWrite from "@/components/whatwear/WhatWearWrite";
import WhatWearCard from "@/components/whatwear/WhatWearCard";
import LoginFilter from "@/components/module/LoginFilter";

export default {
  name: "WhatWear",
  components: {
    WhatWearList,
    WhatWearWrite,
    WhatWearCard,
    LoginFilter,
  },
};
</script>

<style>
</style>