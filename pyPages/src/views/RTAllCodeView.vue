<script setup>
import {onMounted, reactive} from 'vue'
import CommonPage from '@/components/CommonPage.vue'
import CommonTablePart from '@/components/CommonTablePart.vue'
import PeColumn from '@/components/TablePart/PeColumn.vue'
import {cellStyle, headerCellStyle} from "@/api/util";
import {findLast} from "@/api/backend";
import {commonPageRef} from '@/api/commonpage'

const rtData = reactive({});

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
  <CommonPage ref="commonPageRef">
    <template #resTable>
      <el-table :data="rtData.value" class="mt-2"
                :cell-style="cellStyle" max-height="400"
                :header-cell-style="headerCellStyle">
        <el-table-column property="pct_chg" label="pch"/>
        <el-table-column property="change_hand" label="hand"/>
        <PeColumn/>
        <el-table-column property="pb" sortable label="pb"/>
        <el-table-column property="cap" sortable label="cap"/>
        <el-table-column property="ts_code" label="code"/>
        <CommonTablePart/>
      </el-table>
    </template>
  </CommonPage>
</template>

<style scoped>

</style>