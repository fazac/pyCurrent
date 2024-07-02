<script setup>
import {onMounted, reactive} from 'vue'
import CommonPage from '@/components/CommonPage.vue'
import OperateButton from '@/components/OperateButton.vue'
import {cellStyle,headerCellStyle} from "@/api/util";
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
        <el-table-column property="pe" sortable label="pe"/>
        <el-table-column property="pb" sortable label="pb"/>
        <el-table-column property="cap" sortable label="cap"/>
        <el-table-column property="label" min-width="110px" show-overflow-tooltip label="label"/>
        <el-table-column property="ts_code" label="code"/>
        <OperateButton/>
      </el-table>
    </template>
  </CommonPage>
</template>

<style scoped>

</style>