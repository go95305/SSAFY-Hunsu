<template>
  <div>
    {{ click }} {{ "1" }}{{ searchedList }}
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
    <OotdList v-if="click" :key="key" :sortNum="sort" :limitNum="limit" />
  </div>
</template>

<script>
import OotdList from "@/components/ootd/OotdList";
import OotdSearchBtn from "@/components/ootd/OotdSearchBtn";
import OotdWritePage from "@/components/ootd/OotdWritePage";
import OotdFilter from "@/components/ootd/OotdFilter";
import { EventBus } from "@/services/eventBus";
import LoginFilter from "@/components/module/LoginFilter";

export default {
  name: "Ootd",
  props: {
    clicked: {
      default: false,
      type: Boolean,
    },
    searchedListed: {
      defualt: null,
    },
  },
  computed: {
    click() {
      return this.clicked;
    },
    ootdList() {
      return this.searchedListed;
    },
  },
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
      // click: this.clicked,
      // ootdList: this.searchedListed,
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

    console.log("in ootd create", this.click, this.searchedList);
  },
  mounted() {
    console.log("in ootd mounted", this.click, this.searchedList);
  },
};
</script>

<style>
</style>