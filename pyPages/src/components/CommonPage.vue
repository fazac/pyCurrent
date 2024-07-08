<script setup>
import {provide, ref} from 'vue'
import DetailDialog from '../components/DetailDialog.vue'
import LeftToolPanel from '../components/parts/LeftToolPanel.vue'
import LineChart from "@/components/LineChart.vue";
import RightToolPanel from "@/components/parts/RightToolPanel.vue";
import MyTable from "@/components/TablePart/MITable.vue";

const lineCode = ref('');
const lineType = ref('');
const detailCode = ref('');

provide('lineCode', lineCode);
provide('lineType', lineType);
provide('detailCode', detailCode);


</script>

<template>
  <el-container>
    <LeftToolPanel/>
    <el-main class="flex-column">
      <slot name="queryParams"></slot>
      <MyTable/>
      <LineChart/>
    </el-main>
    <RightToolPanel/>
  </el-container>
  <DetailDialog/>
</template>

<style scoped>

</style>