<script setup>
import {findDataByCode, findRocByTypeDate} from "@/api/backend";
import {inject, reactive, ref, watch} from 'vue'
import {cellStyle, headerCellStyle, isEmpty} from "@/api/util";

const dialogTableVisible = ref(false);
const codeTableData = reactive({});
const dialogType = ref('');
const rocType = ref('');

const detailCode = inject('detailCode', null);

const resetCode = () => {
  detailCode.value = null;
  dialogTableVisible.value = false;
}

watch(detailCode, async () => {
  if (!isEmpty(detailCode) && !isEmpty(detailCode.value)) {
    findDataByCode(detailCode.value).then(res => {
      dialogTableVisible.value = true;
      codeTableData.value = res;
      dialogType.value = 'open';
    });
  }
});

watch(rocType, async () => {
  if (!isEmpty(rocType) && !isEmpty(detailCode)) {
    findRocByTypeDate(detailCode.value, rocType.value, '', '').then(res => {
      codeTableData.value.roc = res;
    })
  }
})

function clickRocRow() {

}

</script>

<template>
  <el-dialog v-model="dialogTableVisible" title="DETAIL" :show-close="false" draggable destroy-on-close
             width="1000" :before-close="resetCode" :z-index="9999">
    <el-radio-group class="radio-group" v-model="dialogType" size="large">
      <el-radio-button label="open" value="open"/>
      <el-radio-button label="dn" value="dn"/>
      <el-radio-button label="label" value="label"/>
      <el-radio-button label="roc" value="roc"/>
      <el-radio-button label="current" value="current"/>
    </el-radio-group>

    <el-radio-group class="radio-group" v-if="dialogType==='roc'" v-model="rocType" size="default">
      <el-radio-button label="da" value="1"/>
      <el-radio-button label="ten" value="2"/>
      <el-radio-button label="thirty" value="3"/>
      <el-radio-button label="fifty" value="4"/>
      <el-radio-button label="hundred" value="5"/>
    </el-radio-group>

    <el-table :data="codeTableData.value.open" class="mt-2" v-if="dialogType==='open'"
              :cell-style="cellStyle"
              :header-cell-style="headerCellStyle">
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
              :header-cell-style="headerCellStyle">
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
              :header-cell-style="headerCellStyle">
      <el-table-column property="startDate" label="startDate"/>
      <el-table-column property="endDate" label="endDate"/>
      <el-table-column property="count" label="count"/>
      <el-table-column property="ratio" label="ratio"/>
      <el-table-column property="doorPri" label="openPri"/>
      <el-table-column property="curClosePri" label="closePri"/>
    </el-table>

    <el-table :data="codeTableData.value.current" class="mt-2" max-height="400px" stripe v-if="dialogType==='current'"
              :cell-style="cellStyle"
              :header-cell-style="headerCellStyle">
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