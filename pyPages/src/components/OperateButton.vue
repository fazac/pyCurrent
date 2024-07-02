<script setup>

import {showDetails, showLineChart} from "@/api/commonpage";
import {List, TrendCharts} from '@element-plus/icons-vue'
</script>

<template>
  <el-table-column label="详情" width="130">
    <template #default="scope">
      <el-button type="info" round
                 @click="scope.row.code?showDetails(scope.row.code):scope.row.tsCode?showDetails(scope.row.tsCode):showDetails(scope.row.ts_code)"
                 :icon="List"></el-button>
      <el-button type="info" round
                 @click="scope.row.code?showLineChart(scope.row.code):scope.row.tsCode?showLineChart(scope.row.tsCode):showLineChart(scope.row.ts_code)"
                 :icon="TrendCharts"></el-button>
    </template>
  </el-table-column>
</template>

<style scoped>

</style>