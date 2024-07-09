<script setup>
import {reactive, ref} from 'vue'
import {findOtherConcernList} from "@/api/backend";
import {cellStyle, headerCellStyle, rowStyleClass} from "@/api/util";
import {Compass, List,} from '@element-plus/icons-vue'
import MPTable from '@/components/MPTable.vue'

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

  <el-dialog v-model="otherConcernVisible" title="OTHER" :modal="false"
             :show-close="false" center draggable destroy-on-close width="1000">
    <MPTable :myTableData="otherConcernTableData.value">
      <template #column-slot>
        <el-table-column prop="extraNode.mark" label="mark"/>
        <el-table-column prop="extraNode.rt" label="rt"/>
        <el-table-column prop="extraNode.h" label="h"/>
      </template>
    </MPTable>
  </el-dialog>

</template>

<style scoped>

</style>