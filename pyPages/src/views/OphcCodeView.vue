<script setup>
import {reactive} from 'vue'
import ModelPage from '@/components/ModelPage.vue'
import {isEmpty, nfc, nullArr, tableData} from "@/api/util";
import {searchSome} from "@/api/backend";

const searchObj = reactive(
    {
      hand: null,
      pch: null,
      dayCount: null,
    }
);

const sLimitVOTableData = tableData();

function fetchOPHC() {
  if (isEmpty(searchObj) || isEmpty(searchObj.dayCount) || (isEmpty(searchObj.pch) && isEmpty(searchObj.hand))) {
    nfc('input error', 'some info miss', 'warning')
    return;
  }
  searchSome("4", ...nullArr(3), searchObj.hand, searchObj.pch, searchObj.dayCount).then(res => {
    sLimitVOTableData.value = res.codeDataVOList;
  })
}

</script>

<template>
  <ModelPage>
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
  </ModelPage>
</template>

<style scoped>
</style>