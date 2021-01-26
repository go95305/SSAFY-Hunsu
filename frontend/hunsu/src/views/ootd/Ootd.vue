<template>
  <div>

    <h1>#OOTD</h1>
    <OotdFilter />
    <OotdSearchBtn />
    <OotdWritePage />
    <OotdIList style="margin: 20px" />
    

  </div>
</template>

<script>
import OotdIList from '@/components/ootd/OotdIList'
import OotdSearchBtn from '@/components/ootd/OotdSearchBtn'
import OotdWritePage from '@/components/ootd/OotdWritePage'
import OotdFilter from '@/components/ootd/OotdFilter'


export default {
  name: "Ootd",
  components: {
    OotdIList,
    OotdSearchBtn,
    OotdWritePage,
    OotdFilter,

  }
}
</script>

<style>

</style>