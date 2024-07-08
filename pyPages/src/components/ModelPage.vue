<script setup>
import DetailDialog from '../components/DetailDialog.vue'
import LeftToolPanel from '../components/LeftToolPanel.vue'
import LineChart from "@/components/LineChart.vue";
import RightToolPanel from "@/components/RightToolPanel.vue";
import MITable from "@/components/TablePart/MITable.vue";
</script>

<template>
  <el-container>
    <LeftToolPanel/>
    <el-main class="flex-column">
      <slot name="queryParams"></slot>
      <MITable>
        <template #columnSlot>
          <slot name="elseColumn"></slot>
        </template>
      </MITable>
      <LineChart/>
    </el-main>
    <RightToolPanel/>
  </el-container>
  <DetailDialog/>
</template>

<style scoped>

</style>