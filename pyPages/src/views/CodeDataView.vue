<script setup>
import {onMounted, reactive, ref} from 'vue'
import {ElTable} from 'element-plus'
import LineChart from '../components/LineChart.vue'
import LeftToolPanel from '../components/LeftToolPanel.vue'
import RightToolPanel from '../components/RightToolPanel.vue'
import DetailDialog from '../components/DetailDialog.vue'
import axios from "@/api/http.js";
import {List} from '@element-plus/icons-vue'
import {cellStyle, headerCellStyle, nfc, rowStyleClass} from '@/api/util'


const codeDateList = reactive([{}]);

const code = ref('');
const detailCode = ref('');
const linetype = ref(true);


function handleRowClick(row, column) {
  if (column && column.label === '详情') {
    return;
  }
  code.value = row.tsCode;
}

function handleColumnClick(item) {
  detailCode.value = item;
}

onMounted(() => {
  if (!!window.EventSource) {
    const tabelSource = new EventSource(axios.defaults.baseURL + "/sse/createSSEConnect?clientId=&type=1");
    tabelSource.addEventListener('open', function () {
      console.log("建立table连接");
    })
    tabelSource.onmessage = function (event) {
      if (event.lastEventId !== 'sse_client_id') {
        codeDateList.value = JSON.parse(event.data);
      }
    }
    tabelSource.onerror = function () {
      if (!!tabelSource) {
        tabelSource.close();
      }
    };
    const msgSource = new EventSource(axios.defaults.baseURL + "/sse/createSSEConnect?clientId=&type=3");
    msgSource.addEventListener('open', function () {
      console.log("建立msg连接");
    })
    msgSource.onmessage = function (event) {
      if (event.lastEventId !== 'sse_client_id') {
        let msgArr = JSON.parse(event.data);
        nfc(msgArr[0], msgArr[1]);
      }
    }
    msgSource.onerror = function () {
      if (!!msgSource) {
        msgSource.close();
      }
    };
  }

});


</script>

<template>
  <el-container>
    <LeftToolPanel/>
    <el-main class="flex-column">
      <el-table
          @row-click="handleRowClick"
          :data="codeDateList.value" empty-text=" "
          highlight-current-row max-height="500"
          border
          stripe
          class="flex-grow-0"
          :cell-style="cellStyle"
          :header-cell-style="headerCellStyle"
          :row-class-name="rowStyleClass"
      >
        <el-table-column prop="mark" label="mark"/>
        <el-table-column prop="rt" label="rt"/>
        <el-table-column prop="h" label="h"/>
        <el-table-column prop="cp" label="cp"/>
        <el-table-column prop="bp" label="bp"/>
        <el-table-column prop="rr" label="rr"/>
        <el-table-column prop="cm" label="cm"/>
        <el-table-column prop="pe" label="pe"/>
        <el-table-column prop="tsCode" label="code">
          <template #default="scope">
            <span>{{ scope.row.tsCode.substring(2, 6) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="详情">
          <template #default="scope">
            <el-button type="info" @click.native.stop="handleRowClick" round
                       @click="handleColumnClick(scope.row.tsCode)" :icon="List"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <LineChart :code="code" :dnshow="linetype"/>
    </el-main>
    <RightToolPanel v-model:linetype="linetype" v-model:code="code"/>
  </el-container>

  <DetailDialog v-model:code=detailCode></DetailDialog>


</template>

<style scoped>
</style>