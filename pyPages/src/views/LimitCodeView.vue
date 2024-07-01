<script setup>
import {onMounted, reactive, ref} from 'vue'
import CommonPage from '@/components/CommonPage.vue'
import {nullArr, txtCenter} from "@/api/util";
import {searchSome} from "@/api/backend";
import {List} from '@element-plus/icons-vue'
import {commonPageRef, handleRowClick, showDetails} from '@/api/commonpage'

const searchObj = reactive(
    {
      searchDate: null,
      type: '',
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
                @row-click="handleRowClick"
                :cell-style="txtCenter" max-height="360" stripe v-if="sLimitVOTableData.value !=null"
                :header-cell-style="txtCenter">
        <el-table-column property="code" label="code"/>
        <el-table-column property="labels" min-width="110px" show-overflow-tooltip label="labels"/>
        <el-table-column property="count" sortable label="count"/>
        <el-table-column property="cap" sortable label="cap"/>
        <el-table-column property="pe" sortable label="pe"/>
        <el-table-column property="pb" sortable label="pb"/>
        <el-table-column label="详情">
          <template #default="scope">
            <el-button type="info" @click.native.stop="handleRowClick"
                       @click="showDetails(scope.row.code)" :icon="List"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </template>
  </CommonPage>
</template>

<style scoped>

</style>