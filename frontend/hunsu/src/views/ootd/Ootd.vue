<template>
  <div>
    <div class="ml-2">
      <OotdSearchBtn />
    </div>
    <div class="d-flex justify-space-between">
      <OotdFilter />
      <OotdWritePage />
    </div>
    <OotdList
      :key="key"
      :sortNum="sort"
      :limitNum="limit"
      style="margin: 20px"
    />
    <!-- <OotdScroll /> -->
  </div>
</template>

<script>
import OotdList from "@/components/ootd/OotdList";
import OotdSearchBtn from "@/components/ootd/OotdSearchBtn";
import OotdWritePage from "@/components/ootd/OotdWritePage";
import OotdFilter from "@/components/ootd/OotdFilter";
import { EventBus } from "@/services/eventBus";
// import OotdScroll from '@/components/ootd/OotdScroll'

export default {
  name: "Ootd",
  components: {
    OotdList,
    OotdSearchBtn,
    OotdWritePage,
    OotdFilter,
    // OotdScroll
  },
  data() {
    return {
      sort: 0,
      limit: 0,
      key: 0,
    };
  },
  created() {
    EventBus.$on("recent", () => {
      if (this.sort == 1) {
        this.key++;
        this.sort--;
      }
    });
    EventBus.$on("popular", () => {
      if (this.sort == 0) {
        this.key++;
        this.sort++;
      }
    });
  },
};
</script>

<style>
</style>