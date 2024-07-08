<script setup>
import {inject} from 'vue';
import {cellStyle, headerCellStyle, rowStyleClass} from "@/api/util";
import LabelColumn from "@/components/TablePart/LabelColumn.vue";
import OperateButton from "@/components/TablePart/OperateButton.vue";
import CmColumn from "@/components/TablePart/CmColumn.vue";
import PeColumn from "@/components/TablePart/PeColumn.vue";

const myTableData = inject('myTableData', null);
const myTableHeight = inject('myTableHeight', 400);

</script>

<template>
  <el-table
      :data="myTableData.value"
      v-if="myTableData"
      empty-text=" no data "
      :max-height="myTableHeight"
      border
      stripe
      :row-class-name="rowStyleClass"
      :cell-style="cellStyle"
      :header-cell-style="headerCellStyle"
  >
    <slot name="columnSlot"/>
    <CmColumn/>
    <PeColumn/>
    <el-table-column property="pb" sortable label="pb"/>
<!--    <el-table-column property="tsCode" label="code"/>-->
    <LabelColumn/>
    <OperateButton/>
  </el-table>
</template>

<style scoped>

</style>