<script setup>

import {reactive} from 'vue'
import CommonPage from '@/components/CommonPage.vue'
import OperateButton from '@/components/OperateButton.vue'
import {cellStyle, isEmpty, nfc, nullArr} from "@/api/util";
import {searchSome} from "@/api/backend";
import {commonPageRef} from '@/api/commonpage'

const searchObj = reactive(
    {
      r2LowLimit: null,
      r2HighLimit: null,
      r1LowLimit: null,
      r1HighLimit: null,
    }
);

const sLimitVOTableData = reactive({});

function fetchRoc() {
  if (isEmpty(searchObj.r2LowLimit)
      && isEmpty(searchObj.r2HighLimit)
      && isEmpty(searchObj.r1LowLimit)
      && isEmpty(searchObj.r1HighLimit)
  ) {
    nfc('input error', 'limit miss', 'warning')
    return;
  }
  searchSome("5", ...nullArr(6), searchObj.r2LowLimit, searchObj.r2HighLimit, searchObj.r1LowLimit, searchObj.r1HighLimit)
      .then(res => {
        sLimitVOTableData.value = res;
      }).catch(e => {
    nfc('result error', e, 'error')
  })
}
</script>

<template>
  <CommonPage ref="commonPageRef">
    <template #queryParams>
      <el-form :model="searchObj" inline>
        <el-form-item>
          <el-input-number v-model="searchObj.r2LowLimit" autocomplete="off" placeholder=" - r2 low"/>
        </el-form-item>
        <el-form-item>
          <el-input-number v-model="searchObj.r2HighLimit" autocomplete="off" placeholder=" - r2 high"/>
        </el-form-item>
        <el-form-item>
          <el-input-number v-model="searchObj.r1LowLimit" autocomplete="off" placeholder=" r1 low"/>
        </el-form-item>
        <el-form-item>
          <el-input-number v-model="searchObj.r1HighLimit" autocomplete="off" placeholder=" r1 high"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="fetchRoc">
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
        <el-table-column property="r1" sortable label="last1"/>
        <el-table-column property="r2" sortable label="last2"/>
        <el-table-column property="cap" sortable label="cap"/>
        <el-table-column property="pe" sortable label="pe"/>
        <el-table-column property="pb" sortable label="pb"/>
        <OperateButton/>
      </el-table>
    </template>
  </CommonPage>
</template>

