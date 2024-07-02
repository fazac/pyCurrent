<script setup>
import {ref} from 'vue'
import DetailDialog from '../components/DetailDialog.vue'
import LeftToolPanel from '../components/LeftToolPanel.vue'
import LineChart from "@/components/LineChart.vue";
import RightToolPanel from "@/components/RightToolPanel.vue";

const code = ref('');
const linetype = ref(true);
const searchCode = ref('');

function showDetails(v) {
  searchCode.value = v;
}

function showLineChart(v) {
  code.value = v;
}

defineExpose({showDetails, showLineChart})

</script>

<template>
  <el-container>
    <LeftToolPanel v-model:linetype="linetype" v-model:code="code"/>
    <el-main class="flex-column">
      <slot name="queryParams"></slot>
      <slot name="resTable">
      </slot>
      <LineChart :code="code" :dnshow="linetype"/>
    </el-main>
    <RightToolPanel/>
  </el-container>

  <DetailDialog v-model:code="searchCode"/>
</template>

<style scoped>

</style>