<script setup>
import {reactive, ref} from 'vue'
import {findOtherConcernList} from "@/api/backend";
import {cellStyle, headerCellStyle, rowStyleClass} from "@/api/util";
import {Compass, List,} from '@element-plus/icons-vue'

const otherConcernVisible = ref(false);
const otherConcernTableData = reactive({});
function showOtherConcernDial() {
  findOtherConcernList().then(res => {
    otherConcernTableData.value = res;
    otherConcernVisible.value = true;
  })
}

</script>

<template>
  <el-button type="info" size="large"
             @click="showOtherConcernDial" :icon="Compass"></el-button>

  <el-dialog v-model="otherConcernVisible" title="OTHER"
             :show-close="false" center draggable destroy-on-close width="1000">
    <el-table
        :data="otherConcernTableData.value" empty-text=" " max-height="600"
        border
        stripe
        :row-class-name="rowStyleClass"
        :cell-style="cellStyle"
        :header-cell-style="headerCellStyle"
    >
      <el-table-column prop="mark" label="mark"/>
      <el-table-column prop="rt" label="rt"/>
      <el-table-column prop="h" label="h"/>
      <el-table-column prop="cp" label="cp"/>
      <el-table-column prop="bp" label="bp"/>
      <el-table-column prop="rr" label="rr"/>
      <el-table-column prop="cm" sortable label="cm"/>
      <el-table-column prop="pe" sortable label="pe"/>
      <el-table-column prop="tsCode" label="code">
        <template #default="scope">
          <span>{{ scope.row.tsCode.substring(0, 1).concat(scope.row.tsCode.substring(2, 6)) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="详情">
        <template #default="scope">
          <el-button type="info" round
                     @click="handleColumnClick(scope.row.tsCode)" :icon="List"></el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>

</template>

<style scoped>

</style>