<script setup>
import {reactive, ref, onMounted} from 'vue'
import {useDark, useToggle} from '@vueuse/core'
import {ElTable} from 'element-plus'
import DNLineChart from './DNLineChart.vue'
import RTLineChart from './RTLineChart.vue'
import axios from "@/api/http.js";
import {Search, Edit} from '@element-plus/icons-vue'
import {findDataByCode, findConstants, updateConstant} from '@/api/backend.js'
import {ElNotification} from 'element-plus'

const isDark = useDark()
const toggleDark = useToggle(isDark)

const isDarkType = ref(true);

const codeDateList = reactive([{}]);

const code = ref('');

const lineType = ref(true);

const dialogTableVisible = ref(false);
const codeTableData = reactive({});
const dialogType = ref('');

const constants = reactive({});
const constantsDial = ref(false);
const constantsDialInner = ref(false);
const selectConstant = reactive({});

function handleRowClick(row) {
  code.value = row.tsCode;
}

function handleColumnClick(item) {
  findDataByCode(item.tsCode).then(res => {
    dialogTableVisible.value = true;
    codeTableData.value = res;
    dialogType.value = 'open';
  });
}

onMounted(() => {
  if (!!window.EventSource) {
    window.source = new EventSource(axios.defaults.baseURL + "/sse/createSSEConnect?clientId=");
    window.source.addEventListener('open', function (e) {
      console.log("建立连接");
    })
    window.source.onmessage = function (event) {
      if (event.lastEventId !== 'sse_client_id') {
        codeDateList.value = JSON.parse(event.data);
      }
    }
  }

});

function changeLineType() {
  code.value = '';
}


function cellStyle(row) {
  if (row.columnIndex === 0 || row.columnIndex === 8 || row.columnIndex === 9) {
    return {'text-align': 'center'};
  } else {
    return {'text-align': 'right'};
  }
}

function headerCellStyle() {
  return {'text-align': 'center'};
}

function showConstantsDial() {
  findConstants().then(res => {
    constants.value = res;
    constantsDial.value = true;
  })
}

function handelSelectConstantRow(row) {
  selectConstant.value = row;
  if (selectConstant.value.multiValue !== null) {
    selectConstant.value.multiValueStr = JSON.stringify(selectConstant.value.multiValue);
  }
  constantsDialInner.value = true;
}

function onSubmit() {
  // if (selectConstant.value.multiValueStr !== undefined
  //     && selectConstant.value.multiValueStr !== "null"
  //     && selectConstant.value.multiValueStr.trim() !== '') {
  //   selectConstant.value.multiValue = JSON.parse(selectConstant.value.multiValueStr);
  // }
  // console.log(selectConstant.value);
  updateConstant(selectConstant.value).then(res => {
    if ("ok" === res) {
      constantsDialInner.value = false;
      findConstants().then(res => {
        constants.value = res;
      })
      ElNotification({
        title: 'Success',
        message: 'constant update success',
        type: 'success',
      })
    }
  });
}

</script>

<template>
  <div class="table-container">
    <span class="type-switch">
      <el-switch v-model="lineType" @change="changeLineType" size="large" inline-prompt active-text="dn"
                 inactive-text="rt"
                 style="--el-switch-on-color:  #006699; --el-switch-off-color: #47476b"></el-switch>
      <el-switch v-model="isDarkType" @change="toggleDark()" size="large" inline-prompt active-text="off"
                 inactive-text="on" class="ml-20"
                 style="--el-switch-on-color:  #006699; --el-switch-off-color: #47476b"></el-switch>
      <el-button type="success" size="small" class="ml-20 big-btn"
                 @click="showConstantsDial" :icon="Edit" round></el-button>
    </span>
    <el-table
        @row-click="handleRowClick"
        :data="codeDateList.value" empty-text=" "
        highlight-current-row
        border
        stripe
        class="main-table"
        :cell-style="cellStyle"
        :header-cell-style="headerCellStyle"
    >
      <el-table-column prop="mark" label="mark"/>
      <el-table-column prop="cp" label="cp"/>
      <el-table-column prop="rt" label="rt"/>
      <el-table-column prop="h" label="h"/>
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
          <el-button type="primary" @click.native.stop="handleRowClick" round
                     @click="handleColumnClick(scope.row)" :icon="Search"></el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <DNLineChart :code=code v-if="lineType"></DNLineChart>
  <RTLineChart :code=code v-if="!lineType"></RTLineChart>

  <el-dialog v-model="dialogTableVisible" :show-close="false" lock-scroll draggable destroy-on-close width="1000">
    <div class="type-switch">
      <el-radio-group class="radio-group" v-model="dialogType" size="large">
        <el-radio-button label="open" value="open"/>
        <el-radio-button label="dn" value="dn"/>
        <el-radio-button label="label" value="label"/>
        <el-radio-button label="roc" value="roc"/>
      </el-radio-group>
    </div>

    <el-table :data="codeTableData.value.open" class="mt-2" v-if="dialogType==='open'"
              :cell-style="{'text-align': 'center'}"
              :header-cell-style="{'text-align': 'center'}">
      <el-table-column property="pct_chg" label="pch"/>
      <el-table-column property="change_hand" label="hand"/>
      <el-table-column property="pe" label="pe"/>
      <el-table-column property="pb" label="pb"/>
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
              :cell-style="{'text-align': 'center'}"
              :header-cell-style="{'text-align': 'center'}">
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
              :type="index === 0 ? 'success' :'primary'"
              :size="index === 0 ? 'large' :'default'">
        {{ item }}
      </el-tag>
    </div>

    <el-table :data="codeTableData.value.roc" class="mt-2" max-height="400px" stripe v-if="dialogType==='roc'"
              :cell-style="{'text-align': 'center'}"
              :header-cell-style="{'text-align': 'center'}">
      <el-table-column property="startDate" label="startDate"/>
      <el-table-column property="endDate" label="endDate"/>
      <el-table-column property="count" label="count"/>
      <el-table-column property="ratio" label="ratio"/>
      <el-table-column property="doorPri" label="openPri"/>
      <el-table-column property="curClosePri" label="closePri"/>
    </el-table>

  </el-dialog>

  <el-dialog v-model="constantsDial" title="CONSTANTS"
             :show-close="false" lock-scroll center draggable destroy-on-close width="1000">
    <el-table :data="constants.value" class="mt-2" max-height="400px"
              :cell-style="{'text-align': 'center'}"
              :header-cell-style="{'text-align': 'center'}"
              @row-click="handelSelectConstantRow"
    >
      <el-table-column property="ckey" label="ckey"/>
      <el-table-column property="cvalue" label="cvalue"/>
      <el-table-column prop="mv" show-overflow-tooltip label="multiValue">
        <template #default="scope">
          <span>{{ scope.row.multiValue !== null ? JSON.stringify(scope.row.multiValue) : '' }}</span>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="constantsDialInner" align-center center destroy-on-close
               :show-close="false" width="600">
      <el-form :model="selectConstant">
        <el-form-item label="cval" label-width="88px">
          <el-input v-model="selectConstant.value.cvalue" autocomplete="off" type="textarea"/>
        </el-form-item>
        <el-form-item label="mval"  label-width="88px">
          <el-input v-model="selectConstant.value.multiValueStr" :autosize="{ minRows: 2, maxRows: 4 }" type="textarea">
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="constantsDialInner = false">Cancel</el-button>
          <el-button type="primary" @click="onSubmit">
            Confirm
          </el-button>
        </div>
      </template>
    </el-dialog>
  </el-dialog>
</template>

<style scoped>
</style>