<script setup>
import {onMounted, reactive, ref} from 'vue'
import {nullArr, txtCenter} from "@/api/util";
import {searchSome} from "@/api/backend";
import {List} from '@element-plus/icons-vue'
import DetailDialog from '../components/DetailDialog.vue'
import ToolPanel from '../components/LeftToolPanel.vue'
import LineChart from "@/components/LineChart.vue";
import RightToolPanel from "@/components/RightToolPanel.vue";

const searchObj = reactive(
    {
      searchDate: null,
      type: '',
    }
);

const sLimitVOTableData = reactive({});

const code = ref('');
const linetype = ref(true);
const searchCode = ref('');

function fetchLimit() {
  searchSome("3", ...nullArr(2), searchObj.searchDate != null ? searchObj.searchDate.getTime() : null).then(res => {
    sLimitVOTableData.value = res;
  })
}

function showDetails(v) {
  searchCode.value = v;
}

function handleRowClick(row, column) {
  if (column && column.label === '详情') {
    return;
  }
  code.value = row.code;
}


onMounted(() => {
  fetchLimit();
})


</script>

<template>
  <el-container>
    <ToolPanel/>
    <el-main class="flex-column">
      <el-date-picker
          v-model="searchObj.searchDate"
          type="date"
          placeholder="Last Day Or Pick A Day"
          @change="fetchLimit"
      />
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
      <LineChart :code="code" :dnshow="linetype"/>
    </el-main>
    <RightToolPanel v-model:linetype="linetype" v-model:code="code"/>
  </el-container>

  <DetailDialog v-model:code="searchCode"/>

</template>

<style scoped>

</style>