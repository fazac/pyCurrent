<script setup>
import {reactive} from 'vue'
import CommonPage from '@/components/CommonPage.vue'
import OperateButton from '@/components/OperateButton.vue'
import {cellStyle, isEmpty, nfc, nullArr} from "@/api/util";
import {searchSome} from "@/api/backend";
import {commonPageRef} from '@/api/commonpage'

const searchObj = reactive(
    {
      hand: null,
      pch: null,
      dayCount: null,
    }
);

const sLimitVOTableData = reactive({});


function fetchOPHC() {
  if (isEmpty(searchObj) || isEmpty(searchObj.dayCount) || (isEmpty(searchObj.pch) && isEmpty(searchObj.hand))) {
    nfc('input error', 'some info miss', 'warning')
    return;
  }
  searchSome("4", ...nullArr(3), searchObj.hand, searchObj.pch, searchObj.dayCount).then(res => {
    sLimitVOTableData.value = res;
  })
}

</script>

<template>
  <CommonPage ref="commonPageRef">
    <template #queryParams>
      <el-form :model="searchObj" inline>
        <el-form-item>
          <el-input-number v-model="searchObj.hand" autocomplete="off" placeholder=" hand limit"/>
        </el-form-item>
        <el-form-item>
          <el-input-number v-model="searchObj.pch" autocomplete="off" placeholder=" pch limit"/>
        </el-form-item>
        <el-form-item>
          <el-input-number v-model="searchObj.dayCount" autocomplete="off" placeholder=" count"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="fetchOPHC">
            Search
          </el-button>
        </el-form-item>
      </el-form>

    </template>
    <template #resTable>
      <el-table :data="sLimitVOTableData.value.limitCodeVOList" class="mt-2"
                :cell-style="cellStyle" max-height="400" stripe v-if="sLimitVOTableData.value"
                :header-cell-style="cellStyle">
        <el-table-column property="code" label="code"/>
        <el-table-column property="labels" min-width="110px" show-overflow-tooltip label="labels"/>
        <el-table-column property="hlc" sortable label="hlc"/>
        <el-table-column property="hand" sortable label="hand"/>
        <el-table-column property="ac" sortable label="ac"/>
        <el-table-column property="cc" sortable label="cc"/>
        <el-table-column property="cap" sortable label="cap"/>
        <el-table-column property="pe" sortable label="pe"/>
        <el-table-column property="pb" sortable label="pb"/>
        <OperateButton/>
      </el-table>
    </template>
  </CommonPage>
</template>

<style scoped>
</style>