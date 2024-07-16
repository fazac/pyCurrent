<script setup>
import CurCount from './functionButton/CurCount.vue'
import Search from './functionButton/Search.vue'
import ToggleDark from './functionButton/ToggleDark.vue'
import ToggleLineType from './functionButton/ToggleLineType.vue'
import UpdateConstants from './functionButton/UpdateConstants.vue'
import ToggleCodeDisplay from './functionButton/ToggleCodeDisplay.vue'
import BackendMessage from './functionButton/BackendMessage.vue'
import PageButton from './pageButton/PageButton.vue'
import {HomeFilled, Promotion} from '@element-plus/icons-vue'
</script>

<template>
  <el-aside width="60px">
    <PageButton :icon='HomeFilled'/>
    <ToggleDark/>
    <ToggleLineType/>
    <ToggleCodeDisplay/>
    <UpdateConstants/>
    <CurCount/>
    <PageButton :icon='Promotion' url='ccs' badgeValue="s" badgePosition="right"/>
    <Search/>
    <BackendMessage/>
  </el-aside>
</template>

<style scoped>

</style>