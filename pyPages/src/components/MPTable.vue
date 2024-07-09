<script setup>
import {cellStyle, headerCellStyle, rowStyleClass} from "@/api/util";
import LabelColumn from "@/components/TablePart/LabelColumn.vue";
import OperateButton from "@/components/TablePart/OperateButton.vue";
import CmColumn from "@/components/TablePart/CmColumn.vue";
import PeColumn from "@/components/TablePart/PeColumn.vue";

const props = defineProps(['myTableData', 'myTableHeight'])

</script>

<template>
  <el-table
      :data="props.myTableData"
      empty-text=" no data "
      :max-height="props.myTableHeight || 400"
      border
      stripe
      :row-class-name="rowStyleClass"
      :cell-style="cellStyle"
      :header-cell-style="headerCellStyle"
  >
    <slot name="column-slot"/>
    <el-table-column property="currentPri" sortable label="cp"/>
    <CmColumn/>
    <PeColumn/>
    <el-table-column property="pb" sortable label="pb"/>
    <el-table-column property="code" label="code"/>
    <LabelColumn/>
    <OperateButton/>
  </el-table>
</template>

<style scoped>

</style>