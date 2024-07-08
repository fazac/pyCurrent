<script setup>
import PageButton from './pageButton/PageButton.vue'
import {BellFilled, Compass, DataBoard, Film, HomeFilled, Link, TopRight, Aim, Promotion} from '@element-plus/icons-vue'
</script>

<template>
  <el-aside width="60px">
    <PageButton :icon='HomeFilled'/>
    <PageButton :icon='DataBoard' url='board'/>
    <PageButton :icon='Compass' url='other'/>
    <PageButton :icon='BellFilled' url='ophc'/>
    <PageButton :icon='Film' url='roc'/>
    <PageButton :icon='Link' url='all'/>
    <PageButton :icon='TopRight' url='continuous'/>
    <PageButton :icon='Aim' url='cptr'/>
    <PageButton :icon='Promotion' url='ccs'/>
  </el-aside>
</template>

<style scoped>

</style>