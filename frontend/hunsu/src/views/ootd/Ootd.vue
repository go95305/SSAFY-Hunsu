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
  </div>
</template>

<script>
import OotdList from "@/components/ootd/OotdList";
import OotdListForHashtag from "@/components/ootd/OotdListForHashtag";
import OotdSearchBtn from "@/components/ootd/OotdSearchBtn";
import OotdWritePage from "@/components/ootd/OotdWritePage";
import OotdFilter from "@/components/ootd/OotdFilter";
import { EventBus } from "@/services/eventBus";


export default {
  name: "Ootd",
  components: {
    OotdList,
    OotdSearchBtn,
    OotdWritePage,
    OotdFilter,
  },
  data() {
    return {
      sort: 0,
      limit: 0,
      key: 0,
    };
  },
  computed: {
    ...mapGetters(["getOotdSearchedList"]),

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