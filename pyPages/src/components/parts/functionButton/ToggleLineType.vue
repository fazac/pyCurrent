<script setup>
import {inject} from 'vue'
import {circleLineType} from "@/api/arrayConstants";
import {TrendCharts,} from '@element-plus/icons-vue'

const lineType = inject('lineType', null);
const lineCode = inject('lineCode', null);

function changeLineType() {
  lineType.value = circleLineType(lineType.value);
  lineCode.value = null;
}

</script>

<template>
  <el-badge :value="lineType" v-if="lineType" type="info" :offset="[-50,0]" class="item">
    <el-button type="info" @click="changeLineType" size="large"
               :icon="TrendCharts"></el-button>
  </el-badge>
</template>

<style scoped>

</style>