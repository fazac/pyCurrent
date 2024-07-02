<script setup>
import {onMounted, reactive} from 'vue'
import CommonPage from '@/components/CommonPage.vue'
import OperateButton from '@/components/OperateButton.vue'
import {cellStyle, headerCellStyle, nullArr} from "@/api/util";
import {searchSome} from "@/api/backend";
import {commonPageRef} from '@/api/commonpage'

const searchObj = reactive(
    {
      searchDate: null,
    }
);

const sLimitVOTableData = reactive({});

function fetchLimit() {
  searchSome("3", ...nullArr(2), searchObj.searchDate != null ? searchObj.searchDate.getTime() : null).then(res => {
    sLimitVOTableData.value = res;
  })
}

onMounted(() => {
  fetchLimit();
})

</script>

<template>
  <CommonPage ref="commonPageRef">
    <template #queryParams>
      <el-date-picker
          v-model="searchObj.searchDate"
          type="date"
          placeholder="Last Day Or Pick A Day"
          @change="fetchLimit"
      />
    </template>
    <template #resTable>
      <el-table :data="sLimitVOTableData.value.limitCodeVOList"
                :cell-style="cellStyle" max-height="360" stripe v-if="sLimitVOTableData.value !=null"
                :header-cell-style="headerCellStyle">
        <el-table-column property="code" label="code"/>
        <el-table-column property="labels" min-width="110px" show-overflow-tooltip label="labels"/>
        <el-table-column property="count" sortable label="count"/>
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