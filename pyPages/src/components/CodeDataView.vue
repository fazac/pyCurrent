<script setup>
import {reactive, ref, onMounted} from 'vue'
import {ElTable} from 'element-plus'
import DNLineChart from './DNLineChart.vue'
import RTLineChart from './RTLineChart.vue'
import ToolPanel from './ToolPanel.vue'
import axios from "@/api/http.js";
import {Search, Histogram, Compass, List, TrendCharts, Flag} from '@element-plus/icons-vue'
import {
  findDataByCode,
  findCurcc,
  findOtherConcernList,
  searchSome
} from '@/api/backend.js'
import {isEmpty, nullArr, txtCenter, nfc} from '@/api/util'


const isDarkType = ref(true);

const codeDateList = reactive([{}]);

const code = ref('');

const lineType = ref(true);

const dialogTableVisible = ref(false);
const codeTableData = reactive({});
const dialogType = ref('');


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
      r2LowLimit: null,
      r2HighLimit: null,
      r1LowLimit: null,
      r1HighLimit: null,
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

function changeLineType() {
  lineType.value = !lineType.value;
  code.value = '';
}


function rowStyleClass(row) {
  if (!isEmpty(row)) {
    if (!isEmpty(row.row) && !isEmpty(row.row.mark) && row.row.mark.indexOf('H') > 0) {
      return 'row-hold-mark';
    } else if (row.row.mark.charAt(0) === 'R') {
      return 'row-high-light';
    }
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
    nfc('input error', 'some info miss', 'warning')
  } else {
    switch (searchObj.type) {
      case "1":
        if (isEmpty(searchObj.code)) {
          nfc('input error', 'code miss', 'warning')
          return;
        }
        searchSome("1", searchObj.code).then(res => {
          dialogTableVisible.value = true;
          codeTableData.value = res;
          dialogType.value = 'open';
          searchVisible.value = false;
        });
        break;
      case "2":
        if (isEmpty(searchObj.name)) {
          nfc('input error', 'name miss', 'warning')
          return;
        }
        searchSome("2", null, searchObj.name).then(res => {
          sOpenVOVisible.value = true;
          sOpenVOTableData.value = res;
          searchVisible.value = false;
        });
        break;
      case "3":
        if (isEmpty(searchObj.searchDate)) {
          nfc('input error', 'searchDate miss', 'warning')
          return;
        }
        searchSome("3", ...nullArr(2), searchObj.searchDate.getTime()).then(res => {
          sLimitVOVisible.value = true;
          sLimitVOTableData.value = res;
          sLimitVOType.value = '3';
          searchVisible.value = false;
        })
        break;
      case "4":
        if (isEmpty(searchObj.dayCount)) {
          nfc('input error', 'dayCount miss', 'warning')
          return;
        }
        searchSome("4", ...nullArr(3), searchObj.hand, searchObj.pch, searchObj.dayCount).then(res => {
          sLimitVOVisible.value = true;
          sLimitVOTableData.value = res;
          sLimitVOType.value = '4';
          searchVisible.value = false;
        })
        break;
      case "5":
        if (isEmpty(searchObj.r2LowLimit)
            && isEmpty(searchObj.r2HighLimit)
            && isEmpty(searchObj.r1LowLimit)
            && isEmpty(searchObj.r1HighLimit)
        ) {
          nfc('input error', 'limit miss', 'warning')
          return;
        }
        searchSome("5", ...nullArr(6), searchObj.r2LowLimit, searchObj.r2HighLimit, searchObj.r1LowLimit, searchObj.r1HighLimit)
            .then(res => {
              sLimitVOVisible.value = true;
              sLimitVOTableData.value = res;
              sLimitVOType.value = '5';
              searchVisible.value = false;
            }).catch(e => {
          nfc('result error', e, 'error')
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
  <el-container>
    <el-aside width="60px">
      <ToolPanel/>
    </el-aside>
    <el-main class="flex-column">
      <el-table
          @row-click="handleRowClick"
          :data="codeDateList.value" empty-text=" "
          highlight-current-row max-height="300"
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
      <DNLineChart :code=code v-if="lineType"></DNLineChart>
      <RTLineChart :code=code v-if="!lineType"></RTLineChart>
    </el-main>
    <el-aside width="60px">
      <el-button type="primary" @click="changeLineType" size="large"
                 :icon="lineType?'Flag':'TrendCharts'"></el-button>
      <el-button type="success" size="large"
                 @click="showOtherConcernDial" :icon="Compass"></el-button>
      <el-button type="success" size="large"
                 @click="showCurccDial" :icon="Histogram"></el-button>
      <el-button type="primary" size="large"
                 @click="showSearchDial" :icon="Search"></el-button>
    </el-aside>
  </el-container>

  <el-dialog v-model="dialogTableVisible" title="DETAIL" :show-close="false" draggable destroy-on-close
             width="1000">
    <div class="center-panel">
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

  <el-dialog v-model="curCountVisible" title="CUR-COUNT"
             :show-close="false" center draggable destroy-on-close width="1000">
    <div class="center-panel">
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
    <div class="center-panel">
      <el-radio-group class="radio-group" v-model="searchObj.type" size="large">
        <el-radio-button label="code" value="1"/>
        <el-radio-button label="name" value="2"/>
        <el-radio-button label="limit" value="3"/>
        <el-radio-button label="ophc" value="4"/>
        <el-radio-button label="roc" value="5"/>
      </el-radio-group>
    </div>
    <div class="center-panel mt-2">
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
        <el-form-item v-if="searchObj.type === '5'">
          <el-input-number v-model="searchObj.r2LowLimit" autocomplete="off" placeholder=" - r2 low"/>
        </el-form-item>
        <el-form-item v-if="searchObj.type === '5'">
          <el-input-number v-model="searchObj.r2HighLimit" autocomplete="off" placeholder=" - r2 high"/>
        </el-form-item>
        <el-form-item v-if="searchObj.type === '5'">
          <el-input-number v-model="searchObj.r1LowLimit" autocomplete="off" placeholder=" r1 low"/>
        </el-form-item>
        <el-form-item v-if="searchObj.type === '5'">
          <el-input-number v-model="searchObj.r1HighLimit" autocomplete="off" placeholder=" r1 high"/>
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
      <el-table-column property="r1" v-if="sLimitVOType==='5'" sortable label="last1"/>
      <el-table-column property="r2" v-if="sLimitVOType==='5'" sortable label="last2"/>
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