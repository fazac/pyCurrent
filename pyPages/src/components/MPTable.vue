<script setup>
import {cellStyle, headerCellStyle, isEmpty, rowStyleClass} from "@/api/util";
import OperateButton from "@/components/TablePart/OperateButton.vue";
import CmColumn from "@/components/TablePart/CmColumn.vue";

const props = defineProps(['myTableData', 'myTableHeight'])
const filterPEHandler = (
    value,
    row,
    column
) => {
  const property = column['property'];
  return value > 0 ? row[property] > 0 : row[property] < 0;
}

const filterLabelHandler = (
    value,
    row,
    column
) => {
  const property = column['property'];
  if (isEmpty(row[property])) {
    return false;
  }
  return value > 0 ? row[property].includes('预盈预增') : value < 0 ? row[property].includes('预亏预减') > 0 : !row[property].includes('预盈预增') && !row[property].includes('预亏预减');
}

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
    <el-table-column prop="pe" label="pe" :filter-multiple="false" sortable :filters="[
        { text: '正', value: '1' },
        { text: '负', value: '-1' },
      ]" :filter-method="filterPEHandler" filter-placement="bottom-end" column-key="pe"/>
    <el-table-column property="pb" sortable label="pb"/>
    <el-table-column property="code" sortable label="code"/>
    <el-table-column property="labels" min-width="110px" show-overflow-tooltip label="label"
                     :filters="[
                      { text: '预盈', value: '1' },
                      { text: '无', value: '0' },
                      { text: '预亏', value: '-1' },
                    ]"
                     :filter-method="filterLabelHandler" filter-placement="bottom-end" column-key="labels"/>
    <OperateButton/>
  </el-table>
</template>

<style scoped>

</style>