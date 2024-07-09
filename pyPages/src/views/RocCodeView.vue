<script setup>

import {reactive} from 'vue'
import ModelPage from '@/components/ModelPage.vue'
import {isEmpty, nfc, nullArr, tableData} from "@/api/util";
import {searchSome} from "@/api/backend";

const searchObj = reactive(
    {
      r2LowLimit: null,
      r2HighLimit: null,
      r1LowLimit: null,
      r1HighLimit: null,
    }
);

const sLimitVOTableData = tableData();

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
        sLimitVOTableData.value = res.codeDataVOList;
      }).catch(e => {
    nfc('result error', e, 'error')
  })
}

const filterHandler = (
    value,
    row,
    column
) => {
  return value > 0 ? !isEmpty(row.extraNode.r2) : isEmpty(row.extraNode.r2);
}


</script>

<template>
  <ModelPage>
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
    <template #elseColumn>
      <el-table-column property="extraNode.r1" sortable label="r1"/>
      <el-table-column property="extraNode.r2" :filter-multiple="false" sortable :filters="[
        { text: '有', value: '1' },
        { text: '无', value: '-1' },
      ]" :filter-method="filterHandler" filter-placement="bottom-end" label="r2"/>
    </template>
  </ModelPage>
</template>

