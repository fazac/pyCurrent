<script setup>
import {reactive, ref, onMounted} from 'vue'
import {cellStyle, headerCellStyle, rowStyleClass} from "@/api/util";
import {findOtherConcernList} from "@/api/backend";
import DetailDialog from "@/components/DetailDialog.vue";
import ToolPanel from "@/components/LeftToolPanel.vue";
import RightToolPanel from "@/components/RightToolPanel.vue";
import LineChart from "@/components/LineChart.vue";
import {List} from '@element-plus/icons-vue'

const otherConcernVisible = ref(false);
const otherConcernTableData = reactive({});


const code = ref('');
const linetype = ref(true);
const searchCode = ref('');

function fetchOther() {
  findOtherConcernList().then(res => {
    otherConcernTableData.value = res;
    otherConcernVisible.value = true;
  })
}

function handleRowClick(row, column) {
  if (column && column.label === '详情') {
    return;
  }
  code.value = row.tsCode;
}

function showDetails(v) {
  searchCode.value = v;
}

onMounted(() => {
  fetchOther();
})

</script>

<template>
  <el-container>
    <ToolPanel/>
    <el-main class="flex-column">
      <el-table
          :data="otherConcernTableData.value" empty-text=" " max-height="500"
          @row-click="handleRowClick"
          border
          stripe
          :row-class-name="rowStyleClass"
          :cell-style="cellStyle"
          :header-cell-style="headerCellStyle"
      >
        <el-table-column prop="mark" label="mark"/>
        <el-table-column prop="rt" label="rt"/>
        <el-table-column prop="h" label="h"/>
        <el-table-column prop="cp" label="cp"/>
        <el-table-column prop="cm" sortable label="cm"/>
        <el-table-column prop="pe" sortable label="pe"/>
        <el-table-column prop="tsCode" label="code">
          <template #default="scope">
            <span>{{ scope.row.tsCode.substring(0, 1).concat(scope.row.tsCode.substring(2, 6)) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="详情">
          <template #default="scope">
            <el-button type="info" round @click.native.stop="handleRowClick"
                       @click="showDetails(scope.row.tsCode)" :icon="List"></el-button>
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