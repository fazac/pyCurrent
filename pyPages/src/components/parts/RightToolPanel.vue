<script setup>
import CurCount from './functionButton/CurCount.vue'
import OtherConcern from './functionButton/OtherConcern.vue'
import ReloadPage from './functionButton/ReloadPage.vue'
import Search from './functionButton/Search.vue'
import ToggleDark from './functionButton/ToggleDark.vue'
import ToggleLineType from './functionButton/ToggleLineType.vue'
import UpdateConstants from './functionButton/UpdateConstants.vue'
import ToggleCodeDisplay from './functionButton/ToggleCodeDisplay.vue'
</script>

<template>
  <el-aside width="60px">
    <ToggleDark/>
    <ToggleLineType/>
    <ToggleCodeDisplay/>
    <ReloadPage/>
    <UpdateConstants/>
    <CurCount/>
    <OtherConcern/>
    <Search/>
  </el-aside>
</template>

<style scoped>

</style>