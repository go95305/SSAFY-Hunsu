<template>
  <div>

    <h1>#OOTD</h1>
    <OotdFilter />
    <OotdSearchBtn />
    <OotdWriteBtn />
    <OotdIList />
    

  </div>
</template>

<script>
import OotdIList from '@/components/ootd/OotdIList'
import OotdSearchBtn from '@/components/ootd/OotdSearchBtn'
import OotdWriteBtn from '@/components/ootd/OotdWriteBtn'
import OotdFilter from '@/components/ootd/OotdFilter'


export default {
  name: "Ootd",
  components: {
    OotdIList,
    OotdSearchBtn,
    OotdWriteBtn,
    OotdFilter,

  }
}
</script>

<style>

</style>