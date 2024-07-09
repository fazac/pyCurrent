<script setup>
import {reactive} from 'vue'
import ModelPage from '@/components/ModelPage.vue'
import {isEmpty, nfc, tableData} from "@/api/util";
import {findCptr} from "@/api/backend";

const searchObj = reactive(
    {
      symbol: '',
    }
);

const td = tableData();

function fetchCptr() {
  if (isEmpty(searchObj.symbol)) {
    nfc('input error', 'some info miss', 'warning')
    return;
  }
  findCptr(searchObj.symbol).then(res => {
    td.value = res;
  })
}


</script>

<template>
  <ModelPage>
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
  </ModelPage>
</template>

<style scoped>

</style>