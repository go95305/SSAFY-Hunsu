<template>
  <div>
    <LoginFilter />
    <div class="ml-2">
      <OotdSearchBtn />
    </div>
    <div class="d-flex justify-space-between">
      <OotdFilter />
      <OotdWritePage />
    </div>
    <OotdList
      v-if="!click"
      :key="key"
      :sortNum="sort"
      :limitNum="limit"
      style="margin: 20px"
    />
    <OotdList
      v-if="click"
      :key="key"
      :sortNum="sort"
      :limitNum="limit"
      :click="click"
      :searchedList="ootdList"
    />
  </div>
</template>

<script>
import OotdList from "@/components/ootd/OotdList";
import OotdSearchBtn from "@/components/ootd/OotdSearchBtn";
import OotdWritePage from "@/components/ootd/OotdWritePage";
import OotdFilter from "@/components/ootd/OotdFilter";
import { EventBus } from "@/services/eventBus";
import LoginFilter from "@/components/module/LoginFilter";
import { mapGetters } from "vuex";

export default {
  name: "Ootd",

  components: {
    OotdList,
    OotdSearchBtn,
    OotdWritePage,
    OotdFilter,
    LoginFilter,
  },
  data() {
    return {
      sort: 0,
      limit: 0,
      key: 0,
      click: false,
      ootdList: [],
    };
  },
  computed: {
    ...mapGetters(["getSearchedList"]),
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
    if (this.getSearchedList.length !== 0) {
      this.ootdList = this.getSearchedList;
      this.click = true;
    } else {
      this.click = false;
    }
  },
};
</script>

<style>
</style>