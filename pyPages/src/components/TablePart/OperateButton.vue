<script setup>
import {inject} from 'vue';
import {List, TrendCharts} from '@element-plus/icons-vue'


const lineCode = inject('lineCode', null);
const lineType = inject('lineType', null);
const detailCode = inject('detailCode', null);

function showDetailDial(tsCode) {
  console.log(tsCode)
  detailCode.value = tsCode;
}


</script>

<template>
  <el-table-column label="详情" width="130">
    <template #default="scope">
      <el-button type="info" round
                 @click="showDetailDial(scope.row.tsCode)"
                 :icon="List"></el-button>
<!--      <el-button type="info" round-->
<!--                 @click="scope.row.code?showLineChart(scope.row.code):scope.row.tsCode?showLineChart(scope.row.tsCode):showLineChart(scope.row.ts_code)"-->
<!--                 :icon="TrendCharts"></el-button>-->
    </template>
  </el-table-column>
</template>

<style scoped>

</style>