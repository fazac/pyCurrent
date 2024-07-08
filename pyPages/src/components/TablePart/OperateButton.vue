<script setup>
import {inject} from 'vue';
import {List, TrendCharts} from '@element-plus/icons-vue'


const lineCode = inject('lineCode', null);
const detailCode = inject('detailCode', null);

function showDetailDial(tsCode) {
  detailCode.value = tsCode;
}

function showChartLine(tsCode) {
  lineCode.value = tsCode;
}


</script>

<template>
  <el-table-column label="详情" width="130">
    <template #default="scope">
      <el-button type="info" round
                 @click="showDetailDial(scope.row.code)"
                 :icon="List"></el-button>
      <el-button type="info" round
                 @click="showChartLine(scope.row.code)"
                 :icon="TrendCharts"></el-button>
    </template>
  </el-table-column>
</template>

<style scoped>

</style>