<script setup>
import {findDataByCode} from "@/api/backend";
import {reactive, ref, watch} from 'vue'
import {cellStyle, isEmpty, nfc} from "@/api/util";

const dialogTableVisible = ref(false);
const codeTableData = reactive({});
const dialogType = ref('');

const props = defineProps(['code'])
const emit = defineEmits(['update:code'])

const handleColumnClick = () => {
  if (isEmpty(props.code)) {
    nfc("NO CODE", "code is required", "warning")
    return;
  }
  findDataByCode(props.code).then(res => {
    dialogTableVisible.value = true;
    codeTableData.value = res;
    dialogType.value = 'open';
  });
}

const resetCode = () => {
  emit('update:code', '');
  dialogTableVisible.value = false;
}

watch(() => props.code, async () => {
  if (!isEmpty(props.code)) {
    handleColumnClick();
  }
});

</script>

<template>
  <el-dialog v-model="dialogTableVisible" title="DETAIL" :show-close="false" draggable destroy-on-close
             width="1000" :before-close="resetCode">
    <el-radio-group class="radio-group" v-model="dialogType" size="large">
      <el-radio-button label="open" value="open"/>
      <el-radio-button label="dn" value="dn"/>
      <el-radio-button label="label" value="label"/>
      <el-radio-button label="roc" value="roc"/>
      <el-radio-button label="current" value="current"/>
    </el-radio-group>

    <el-table :data="codeTableData.value.open" class="mt-2" v-if="dialogType==='open'"
              :cell-style="cellStyle"
              :header-cell-style="cellStyle">
      <el-table-column property="pct_chg" label="pch"/>
      <el-table-column property="change_hand" label="hand"/>
      <el-table-column property="pe" label="pe"/>
      <el-table-column property="pb" label="pb"/>
      <el-table-column property="cap" label="cap"/>
      <el-table-column property="open" label="open"/>
      <el-table-column property="low" label="low"/>
      <el-table-column property="high" label="high"/>
      <el-table-column property="current_pri" label="cp"/>
      <el-table-column property="avg_pri" label="ap"/>
      <el-table-column property="pri_pre" label="pp"/>
    </el-table>

    <el-table :data="codeTableData.value.dnDetail" class="mt-2" max-height="400px" stripe v-if="dialogType==='dn'"
              :cell-style="cellStyle"
              :header-cell-style="cellStyle">
      <el-table-column prop="tradeDate" sortable label="date"/>
      <el-table-column prop="pctChg" label="pch"/>
      <el-table-column prop="changeHand" label="hand"/>
      <el-table-column prop="priClose" label="close"/>
      <el-table-column prop="priHigh" label="high"/>
      <el-table-column prop="priLow" label="low"/>
      <el-table-column prop="priOpen" label="open"/>
      <el-table-column prop="avg" label="avg">
        <template #default="scope">
          <span>{{ (scope.row.amount / scope.row.vol / 100).toFixed(2) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <div class="gap-2 mt-2" v-if="dialogType==='label'">
      <el-tag v-for="(item,index) in codeTableData.value.label"
              :type="index === 0 || index === 1? 'success' :'primary'"
              :size="index === 0 ? 'large' :'default'">
        {{ item }}
      </el-tag>
    </div>

    <el-table :data="codeTableData.value.roc" class="mt-2" max-height="400px" stripe v-if="dialogType==='roc'"
              :cell-style="cellStyle"
              :header-cell-style="cellStyle">
      <el-table-column property="startDate" label="startDate"/>
      <el-table-column property="endDate" label="endDate"/>
      <el-table-column property="count" label="count"/>
      <el-table-column property="ratio" label="ratio"/>
      <el-table-column property="doorPri" label="openPri"/>
      <el-table-column property="curClosePri" label="closePri"/>
    </el-table>

    <el-table :data="codeTableData.value.current" class="mt-2" max-height="400px" stripe v-if="dialogType==='current'"
              :cell-style="cellStyle"
              :header-cell-style="cellStyle">
      <el-table-column property="di" label="di"/>
      <el-table-column property="rt" label="rt"/>
      <el-table-column property="h" label="h"/>
      <el-table-column prop="bar" label="bar">
        <template #default="scope">
          <span>{{ (scope.row.bar * 1000).toFixed(1) }}</span>
        </template>
      </el-table-column>
      <el-table-column property="vs" label="vs"/>
      <el-table-column property="cp" label="cp"/>
    </el-table>

  </el-dialog>

</template>

<style scoped>

</style>