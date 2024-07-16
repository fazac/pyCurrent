<script setup>
import PageButton from './pageButton/PageButton.vue'
import {Aim, BellFilled, Compass, DataBoard, Film, HomeFilled, Link, TopRight} from '@element-plus/icons-vue'
</script>

<template>
  <el-aside width="60px">
    <PageButton :icon='HomeFilled'/>
    <PageButton :icon='Compass' url='other'/>
    <PageButton :icon='Link' url='all' badgeValue="a"/>
    <PageButton :icon='DataBoard' badgeValue="b" url='board'/>
    <PageButton :icon='TopRight' url='continuous' badgeValue="u"/>
    <PageButton :icon='BellFilled' url='ophc' badgeValue="p"/>
    <PageButton :icon='Film' url='roc' badgeValue="r"/>
    <PageButton :icon='Aim' url='cptr' badgeValue="l"/>
  </el-aside>
</template>

<style scoped>

</style>