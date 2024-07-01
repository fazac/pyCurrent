<script setup>
import {ref} from 'vue'
import DetailDialog from '../components/DetailDialog.vue'
import ToolPanel from '../components/LeftToolPanel.vue'
import LineChart from "@/components/LineChart.vue";
import RightToolPanel from "@/components/RightToolPanel.vue";

const code = ref('');
const linetype = ref(true);
const searchCode = ref('');

function showDetails(v) {
  searchCode.value = v;
}

function handleRowClick(row, column) {
  if (column && column.label === '详情') {
    return;
  }
  code.value = row.code;
}

defineExpose({showDetails, handleRowClick})

</script>

<template>
  <el-container>
    <ToolPanel/>
    <el-main class="flex-column">
      <slot name="queryParams"></slot>
      <slot name="resTable"></slot>
      <LineChart :code="code" :dnshow="linetype"/>
    </el-main>
    <RightToolPanel v-model:linetype="linetype" v-model:code="code"/>
  </el-container>

  <DetailDialog v-model:code="searchCode"/>
</template>

<style scoped>

</style>