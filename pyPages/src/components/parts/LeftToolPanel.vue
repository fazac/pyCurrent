<script setup>
import PageButton from './pageButton/PageButton.vue'
import {BellFilled, Compass, DataBoard, Film, HomeFilled, Link, TopRight, Aim, Promotion} from '@element-plus/icons-vue'
</script>

<template>
  <el-aside width="60px">
    <PageButton :icon='HomeFilled'/>
    <PageButton :icon='DataBoard' badgeValue="b" url='board'/>
    <PageButton :icon='Compass' url='other'/>
    <PageButton :icon='BellFilled' url='ophc' badgeValue="p"/>
    <PageButton :icon='Film' url='roc' badgeValue="r"/>
    <PageButton :icon='Link' url='all' badgeValue="a"/>
    <PageButton :icon='TopRight' url='continuous' badgeValue="u"/>
    <PageButton :icon='Aim' url='cptr' badgeValue="m"/>
    <PageButton :icon='Promotion' url='ccs' badgeValue="s"/>
  </el-aside>
</template>

<style scoped>

</style>