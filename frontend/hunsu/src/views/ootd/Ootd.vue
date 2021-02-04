<template>
<div>
    <OotdFilter />
  <div class="d-flex flex-row-reverse">
    <div class="d-inline-block mx-5 mt-4">
    <OotdWritePage />
    </div>
    <div class="d-inline-block">
    <OotdSearchBtn />
    </div>
  </div>
    <OotdList style="margin: 20px" />
    <!-- <OotdScroll /> -->
    

</div>
</template>

<script>
import OotdList from '@/components/ootd/OotdList'
import OotdSearchBtn from '@/components/ootd/OotdSearchBtn'
import OotdWritePage from '@/components/ootd/OotdWritePage'
import OotdFilter from '@/components/ootd/OotdFilter'
// import OotdScroll from '@/components/ootd/OotdScroll'


export default {
  name: "Ootd",
  components: {
    OotdList,
    OotdSearchBtn,
    OotdWritePage,
    OotdFilter,
    // OotdScroll

  }
}
</script>

<style>

</style>