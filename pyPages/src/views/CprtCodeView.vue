<script setup>
import {reactive} from 'vue'
import CommonPage from '@/components/CommonPage.vue'
import OperateButton from '@/components/OperateButton.vue'
import {cellStyle, headerCellStyle, isEmpty, nfc} from "@/api/util";
import {findCptr} from "@/api/backend";
import {commonPageRef} from '@/api/commonpage'

const searchObj = reactive(
    {
      symbol: '',
    }
);

const tableData = reactive({});

function fetchCptr() {
  if (isEmpty(searchObj.symbol)) {
    nfc('input error', 'some info miss', 'warning')
    return;
  }
  findCptr(searchObj.symbol).then(res => {
    tableData.value = res;
  })
}


</script>

<template>
  <CommonPage ref="commonPageRef">
    <template #queryParams>
      <el-form :model="searchObj" inline>
        <el-form-item>
          <el-input v-model="searchObj.symbol" autocomplete="off" placeholder=" symbol "/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="fetchCptr">
            Search
          </el-button>
        </el-form-item>
      </el-form>

    </template>
    <template #resTable>
      <el-table :data="tableData.value"
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