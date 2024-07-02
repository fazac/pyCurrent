<script setup>
import {onMounted, reactive} from 'vue'
import {cellStyle, headerCellStyle, rowStyleClass} from "@/api/util";
import {findOtherConcernList} from "@/api/backend";
import CommonPage from "@/components/CommonPage.vue";
import {commonPageRef} from '@/api/commonpage'
import OperateButton from '@/components/OperateButton.vue'

const otherConcernTableData = reactive({});

function fetchOther() {
  findOtherConcernList().then(res => {
    otherConcernTableData.value = res;
  })
}

onMounted(() => {
  fetchOther();
})

</script>

<template>
  <CommonPage ref="commonPageRef">
    <template #resTable>
      <el-table
          :data="otherConcernTableData.value" empty-text=" " max-height="500"
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
        <el-table-column prop="cm" sortable label="cm"/>
        <el-table-column prop="pe" sortable label="pe"/>
        <el-table-column prop="tsCode" label="code">
          <template #default="scope">
            <span>{{ scope.row.tsCode.substring(0, 1).concat(scope.row.tsCode.substring(2, 6)) }}</span>
          </template>
        </el-table-column>
        <OperateButton/>
      </el-table>
    </template>
  </CommonPage>
</template>

<style scoped>

</style>