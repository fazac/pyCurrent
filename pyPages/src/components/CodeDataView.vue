<script setup>
import {reactive, ref, onMounted} from 'vue'
import {useDark, useToggle} from '@vueuse/core'
import {ElTable} from 'element-plus'
import DNLineChart from './DNLineChart.vue'
import RTLineChart from './RTLineChart.vue'
import axios from "@/api/http.js";
import {Search, Edit, Histogram, Compass, SwitchButton, List} from '@element-plus/icons-vue'
import {
  findDataByCode,
  findConstants,
  updateConstant,
  findCurcc,
  findOtherConcernList,
  searchSome
} from '@/api/backend.js'
import {isEmpty} from '@/api/util'
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

const curCountVisible = ref(false);
const curCountTableData = reactive({});
const curType = ref('');

const searchVisible = ref(false);
const searchObj = reactive(
    {
      code: '',
      name: '',
      searchDate: null,
      hand: null,
      pch: null,
      dayCount: null,
      type: '',
    }
);

const otherConcernVisible = ref(false);
const otherConcernTableData = reactive({});

const sOpenVOVisible = ref(false);
const sOpenVOTableData = reactive({});

const sLimitVOVisible = ref(false);
const sLimitVOType = ref('');
const sLimitVOTableData = reactive({});


function txtCenter() {
  return {'text-align': 'center'};
}


function handleRowClick(row, column, event) {
  if (column && column.label === '详情') {
    return;
  }
  code.value = row.tsCode;
}

function handleColumnClick(item) {
  findDataByCode(item).then(res => {
    dialogTableVisible.value = true;
    codeTableData.value = res;
    dialogType.value = 'open';
  });
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
        ElNotification({
          title: msgArr[0],
          message: msgArr[1],
          type: 'success',
        })
      }
    }
    msgSource.onerror = function () {
      if (!!msgSource) {
        msgSource.close();
      }
    };
  }

});

function changeLineType() {
  code.value = '';
}


function rowStyleClass(row) {
  if (!isEmpty(row) && row.row.mark.charAt(0) === 'R') {
    return 'row-high-light';
  }
}

function cellStyle(row) {
  if (row.columnIndex === 0 || row.columnIndex === 8 || row.columnIndex === 9) {
    return txtCenter;
  } else {
    return {'text-align': 'right'};
  }
}

function headerCellStyle() {
  return txtCenter;
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

function showCurccDial() {
  findCurcc().then(res => {
    curCountVisible.value = true;
    curCountTableData.value = res;
    curType.value = '3';
  })
}

function showSearchDial() {
  searchVisible.value = true;
  searchObj.type = '1';
  searchObj.searchDate = new Date();
}

function onSearchSubmit() {
  if (isEmpty(searchObj)) {
    ElNotification({
      title: 'input error',
      message: 'some info miss',
      type: 'warning',
    })
  } else {
    switch (searchObj.type) {
      case "1":
        searchSome("1", searchObj.code).then(res => {
          dialogTableVisible.value = true;
          codeTableData.value = res;
          dialogType.value = 'open';
          searchVisible.value = false;
        });
        break;
      case "2":
        searchSome("2", null, searchObj.name).then(res => {
          sOpenVOVisible.value = true;
          sOpenVOTableData.value = res;
          searchVisible.value = false;
        });
        break;
      case "3":
        searchSome("3", null, null, searchObj.searchDate.getTime()).then(res => {
          sLimitVOVisible.value = true;
          sLimitVOTableData.value = res;
          sLimitVOType.value = '3';
          searchVisible.value = false;
        })
        break;
      case "4":
        if (isEmpty(searchObj.dayCount)) {
          ElNotification({
            title: 'input error',
            message: 'dayCount miss',
            type: 'warning',
          })
          return;
        }
        searchSome("4", null, null, null, searchObj.hand, searchObj.pch, searchObj.dayCount).then(res => {
          sLimitVOVisible.value = true;
          sLimitVOTableData.value = res;
          sLimitVOType.value = '4';
          searchVisible.value = false;
        })
        break;
    }

  }
}


function showOtherConcernDial() {
  findOtherConcernList().then(res => {
    otherConcernTableData.value = res;
    otherConcernVisible.value = true;
  })
}

</script>

<template>
  <div class="table-container">
    <span class="type-switch">
      <el-switch v-model="lineType" @change="changeLineType" size="large" inline-prompt active-text="dn"
                 inactive-text="rt"
                 style="--el-switch-on-color:  #006699; --el-switch-off-color: #47476b"></el-switch>
      <!--      <el-switch v-model="isDarkType" @change="toggleDark()" size="large" inline-prompt active-text="off"-->
      <!--                 inactive-text="on"-->
      <!--                 style="&#45;&#45;el-switch-on-color:  #006699; &#45;&#45;el-switch-off-color: #47476b"></el-switch>-->
      <el-button type="primary" size="small" class="big-btn"
                 @click="toggleDark()" :icon="SwitchButton" round></el-button>
      <el-button type="success" size="small" class="big-btn"
                 @click="showOtherConcernDial" :icon="Compass" round></el-button>
      <el-button type="success" size="small" class="big-btn"
                 @click="showCurccDial" :icon="Histogram" round></el-button>
      <el-button type="primary" size="small" class="big-btn"
                 @click="showConstantsDial" :icon="Edit" round></el-button>
      <el-button type="primary" size="small" class="big-btn"
                 @click="showSearchDial" :icon="Search" round></el-button>

    </span>
    <el-table
        @row-click="handleRowClick"
        :data="codeDateList.value" empty-text=" "
        highlight-current-row max-height="300"
        border
        stripe
        class="main-table"
        :cell-style="cellStyle"
        :header-cell-style="headerCellStyle"
        :row-class-name="rowStyleClass"
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
          <el-button type="info" @click.native.stop="handleRowClick" round
                     @click="handleColumnClick(scope.row.tsCode)" :icon="List"></el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <DNLineChart :code=code v-if="lineType"></DNLineChart>
  <RTLineChart :code=code v-if="!lineType"></RTLineChart>

  <el-dialog v-model="dialogTableVisible" title="DETAIL" :show-close="false" draggable destroy-on-close
             width="1000">
    <div class="type-switch">
      <el-radio-group class="radio-group" v-model="dialogType" size="large">
        <el-radio-button label="open" value="open"/>
        <el-radio-button label="dn" value="dn"/>
        <el-radio-button label="label" value="label"/>
        <el-radio-button label="roc" value="roc"/>
        <el-radio-button label="current" value="current"/>
      </el-radio-group>
    </div>

    <el-table :data="codeTableData.value.open" class="mt-2" v-if="dialogType==='open'"
              :cell-style="txtCenter"
              :header-cell-style="txtCenter">
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
              :cell-style="txtCenter"
              :header-cell-style="txtCenter">
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
              :cell-style="txtCenter"
              :header-cell-style="txtCenter">
      <el-table-column property="startDate" label="startDate"/>
      <el-table-column property="endDate" label="endDate"/>
      <el-table-column property="count" label="count"/>
      <el-table-column property="ratio" label="ratio"/>
      <el-table-column property="doorPri" label="openPri"/>
      <el-table-column property="curClosePri" label="closePri"/>
    </el-table>

    <el-table :data="codeTableData.value.current" class="mt-2" max-height="400px" stripe v-if="dialogType==='current'"
              :cell-style="txtCenter"
              :header-cell-style="txtCenter">
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

  <el-dialog v-model="constantsDial" title="CONSTANT"
             :show-close="false" center draggable destroy-on-close width="1000">
    <el-table :data="constants.value" class="mt-2" max-height="400px"
              :cell-style="txtCenter"
              :header-cell-style="txtCenter"
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
        <el-form-item label="mval" label-width="88px">
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

  <el-dialog v-model="curCountVisible" title="CUR-COUNT"
             :show-close="false" center draggable destroy-on-close width="1000">
    <div class="type-switch">
      <el-radio-group class="radio-group" v-model="curType" size="large">
        <el-radio-button label="3" value="3"/>
        <el-radio-button label="6" value="6"/>
        <el-radio-button label="0" value="0"/>
      </el-radio-group>
    </div>
    <el-table :data="curCountTableData.value" class="mt-2" max-height="400px"
              :cell-style="txtCenter" stripe
              :header-cell-style="txtCenter"
              v-if="curType==='3'"
    >
      <el-table-column prop="c30a" label="a"/>
      <el-table-column prop="c30u" label="u"/>
      <el-table-column prop="c305u" label="5u"/>
      <el-table-column prop="c3035u" label="53u"/>
      <el-table-column prop="c3013u" label="31u"/>
      <el-table-column prop="c3001u" label="10u"/>
      <el-table-column prop="c3001d" label="01d"/>
      <el-table-column prop="c3013d" label="13d"/>
      <el-table-column prop="c3037d" label="37d"/>
      <el-table-column prop="c307d" label="7d"/>
    </el-table>
    <el-table :data="curCountTableData.value" class="mt-2" max-height="400px"
              :cell-style="txtCenter" stripe
              :header-cell-style="txtCenter"
              v-if="curType==='6'"
    >
      <el-table-column prop="c60a" label="a"/>
      <el-table-column prop="c60u" label="u"/>
      <el-table-column prop="c605u" label="5u"/>
      <el-table-column prop="c6035u" label="53u"/>
      <el-table-column prop="c6013u" label="31u"/>
      <el-table-column prop="c6001u" label="10u"/>
      <el-table-column prop="c6001d" label="01d"/>
      <el-table-column prop="c6013d" label="13d"/>
      <el-table-column prop="c6037d" label="37d"/>
      <el-table-column prop="c607d" label="7d"/>
    </el-table>
    <el-table :data="curCountTableData.value" class="mt-2" max-height="400px"
              :cell-style="txtCenter" stripe
              :header-cell-style="txtCenter"
              v-if="curType==='0'"
    >
      <el-table-column prop="c00a" label="a"/>
      <el-table-column prop="c00u" label="u"/>
      <el-table-column prop="c005u" label="5u"/>
      <el-table-column prop="c0035u" label="53u"/>
      <el-table-column prop="c0013u" label="31u"/>
      <el-table-column prop="c0001u" label="10u"/>
      <el-table-column prop="c0001d" label="01d"/>
      <el-table-column prop="c0013d" label="13d"/>
      <el-table-column prop="c0037d" label="37d"/>
      <el-table-column prop="c007d" label="7d"/>

    </el-table>
  </el-dialog>

  <el-dialog v-model="searchVisible" title="SEARCH"
             :show-close="false" center draggable destroy-on-close width="800">
    <div class="type-switch">
      <el-radio-group class="radio-group" v-model="searchObj.type" size="large">
        <el-radio-button label="code" value="1"/>
        <el-radio-button label="name" value="2"/>
        <el-radio-button label="limit" value="3"/>
        <el-radio-button label="ophc" value="4"/>
      </el-radio-group>
    </div>
    <div class="type-switch mt-2">
      <el-form :model="searchObj">
        <el-form-item v-if="searchObj.type === '1'">
          <el-input v-model="searchObj.code" autocomplete="off" placeholder="please input code"/>
        </el-form-item>
        <el-form-item v-if="searchObj.type === '2'">
          <el-input v-model="searchObj.name" autocomplete="off" placeholder="please input name"/>
        </el-form-item>
        <el-form-item v-if="searchObj.type === '3'">
          <el-date-picker
              v-model="searchObj.searchDate"
              type="date"
              placeholder="Pick a day"
          />
        </el-form-item>
        <el-form-item v-if="searchObj.type === '4'">
          <el-input-number v-model="searchObj.hand" autocomplete="off" placeholder=" hand limit"/>
        </el-form-item>
        <el-form-item v-if="searchObj.type === '4'">
          <el-input-number v-model="searchObj.pch" autocomplete="off" placeholder=" pch limit"/>
        </el-form-item>
        <el-form-item v-if="searchObj.type === '4'">
          <el-input-number v-model="searchObj.dayCount" autocomplete="off" placeholder=" count"/>
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="searchVisible = false">Cancel</el-button>
        <el-button type="primary" icon="Search" @click="onSearchSubmit">
          Search
        </el-button>
      </div>
    </template>
  </el-dialog>

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
      <el-table-column prop="cp" label="cp"/>
      <el-table-column prop="rt" label="rt"/>
      <el-table-column prop="h" label="h"/>
      <el-table-column prop="bp" label="bp"/>
      <el-table-column prop="rr" label="rr"/>
      <el-table-column prop="cm" label="cm"/>
      <el-table-column prop="pe" label="pe"/>
      <el-table-column prop="tsCode" label="code">
        <template #default="scope">
          <span>{{ scope.row.tsCode.substring(0, 1).concat(scope.row.tsCode.substring(2, 6)) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="详情">
        <template #default="scope">
          <el-button type="info" @click.native.stop="handleRowClick" round
                     @click="handleColumnClick(scope.row.tsCode)" :icon="List"></el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>

  <el-dialog v-model="sOpenVOVisible" title="NAME-SEARCH" :show-close="false" draggable destroy-on-close
             width="1000">
    <el-table :data="sOpenVOTableData.value.openVOList" class="mt-2"
              :cell-style="txtCenter"
              :header-cell-style="txtCenter">
      <el-table-column property="pct_chg" label="pch"/>
      <el-table-column property="change_hand" label="hand"/>
      <el-table-column property="pe" label="pe"/>
      <el-table-column property="pb" label="pb"/>
      <el-table-column property="cap" label="cap"/>
      <el-table-column property="current_pri" label="cp"/>
      <el-table-column property="avg_pri" label="ap"/>
      <el-table-column property="pri_pre" label="pp"/>
      <el-table-column label="详情">
        <template #default="scope">
          <el-button type="info"
                     @click="handleColumnClick(scope.row.ts_code)" :icon="List"></el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>

  <el-dialog v-model="sLimitVOVisible" title="LIMIT-SEARCH" :show-close="false" draggable destroy-on-close
             width="1000">
    <el-table :data="sLimitVOTableData.value.limitCodeVOList" class="mt-2"
              :cell-style="txtCenter" max-height="400" stripe
              :header-cell-style="txtCenter">
      <el-table-column property="code" label="code"/>
      <el-table-column property="labels" min-width="110px" show-overflow-tooltip label="labels"/>
      <el-table-column property="count" v-if="sLimitVOType==='3'" sortable label="count"/>
      <el-table-column property="hlc" v-if="sLimitVOType==='4'" sortable label="hlc"/>
      <el-table-column property="hand" v-if="sLimitVOType==='4'" sortable label="hand"/>
      <el-table-column property="ac" v-if="sLimitVOType==='4'" sortable label="ac"/>
      <el-table-column property="cc" v-if="sLimitVOType==='4'" sortable label="cc"/>
      <el-table-column property="cap" sortable label="cap"/>
      <el-table-column property="pe" sortable label="pe"/>
      <el-table-column property="pb" sortable label="pb"/>
      <el-table-column label="详情">
        <template #default="scope">
          <el-button type="info"
                     @click="handleColumnClick(scope.row.code)" :icon="List"></el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<style scoped>
</style>