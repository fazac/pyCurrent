<script setup>
import {onMounted} from 'vue'
import ModelPage from '@/components/ModelPage.vue'
import {cellStyle, headerCellStyle, tableData} from "@/api/util";
import {findLast} from "@/api/backend";

const rtData = tableData();

function fetchRtAll() {
  findLast().then(res => {
    rtData.value = res;
  })
}

onMounted(() => {
  fetchRtAll();
})
</script>

<template>
  <ModelPage>
    <template #elseColumn>
      <el-table-column property="extraNode.pch" label="rt"/>
      <el-table-column property="extraNode.hand" label="h"/>
      <el-table-column property="extraNode.avgPri" label="ap"/>
      <el-table-column property="extraNode.priPre" label="pp"/>
    </template>
  </ModelPage>
</template>

<style scoped>

</style>