<script setup>
import {inject, useSlots} from 'vue';
import {cellStyle, extraTdKey, headerCellStyle, rowStyleClass} from "@/api/util";
import LabelColumn from "@/components/TablePart/LabelColumn.vue";
import OperateButton from "@/components/TablePart/OperateButton.vue";
import CmColumn from "@/components/TablePart/CmColumn.vue";
import PeColumn from "@/components/TablePart/PeColumn.vue";

const props = defineProps(['elseColumn'])

const myTableData = inject('myTableData', null);
const myTableHeight = inject('myTableHeight', 400);
const slotColumn = !!useSlots().columnSlot && props.elseColumn;

</script>

<template>
  <el-table
      :data="myTableData.value"
      v-if="myTableData"
      empty-text=" no data "
      :max-height="myTableHeight"
      class="mt-1p"
      border
      stripe
      :row-class-name="rowStyleClass"
      :cell-style="cellStyle"
      :header-cell-style="headerCellStyle"
  >
    <slot name="columnSlot"/>
    <el-table-column :sortable="item.sortable"
                     v-if="!slotColumn"
                     v-for="item in extraTdKey(myTableData)"
                     :key="item.fullProp"
                     :label="item.prop"
                     :prop="item.fullProp">
    </el-table-column>
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