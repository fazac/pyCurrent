<script setup>
import {inject} from 'vue'
import {circleLineType} from "@/api/arrayConstants";
import {TrendCharts,} from '@element-plus/icons-vue'

const lineType = inject('lineType', null);

function changeLineType() {
  lineType.value = circleLineType(lineType.value);
}

</script>

<template>
  {{lineType}}
  <el-button type="info" @click="changeLineType" size="large"
             :icon="TrendCharts"></el-button>
</template>

<style scoped>

</style>