<script setup>

import {onMounted, reactive} from 'vue'
import CommonPage from '@/components/CommonPage.vue'
import CommonTablePart from '@/components/CommonTablePart.vue'
import {cellStyle, headerCellStyle} from "@/api/util";
import {findLastContinuous} from "@/api/backend";
import {commonPageRef} from '@/api/commonpage'

const tableData = reactive({});

function fetchContinuous() {
  findLastContinuous().then(res => {
    console.log(res)
    tableData.value = res;
  })
}

onMounted(() => {
  fetchContinuous();
})

</script>

<template>
  <CommonPage ref="commonPageRef">
    <template #resTable>
      <el-table :data="tableData.value"
                :cell-style="cellStyle" max-height="360" stripe v-if="tableData.value"
                :header-cell-style="headerCellStyle">
        <el-table-column property="tsCode" label="code"/>
        <el-table-column property="upDays" sortable label="upDays"/>
        <el-table-column property="upPer" sortable label="upPer"/>
        <el-table-column property="changeHand" sortable label="hand"/>
        <el-table-column property="priClose"  label="priClose"/>
        <CommonTablePart/>
      </el-table>
    </template>
  </CommonPage>
</template>

<style scoped>

</style>