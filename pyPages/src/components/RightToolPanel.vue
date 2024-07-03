<script setup>

import {reactive, ref} from 'vue'
import {ElTable} from 'element-plus'
import {useDark, useToggle} from '@vueuse/core'
import {Compass, Edit, Histogram, List, Refresh, Search, SwitchButton,} from '@element-plus/icons-vue'
import {cellStyle, headerCellStyle, isEmpty, nfc, nullArr, rowStyleClass} from "@/api/util";
import {findConstants, findCurcc, findOtherConcernList, searchSome, updateConstant} from "@/api/backend";
import DetailDialog from '../components/DetailDialog.vue';




const isDark = useDark()
const toggleDark = useToggle(isDark)

const props = defineProps(['linetype', 'code'])
const emit = defineEmits(['update:linetype', 'update:code'])

function changeLineType() {
  emit('update:linetype', !props.linetype)
  emit('update:code', '')
}


function reloadPage() {
  window.location.reload();
}

const constants = reactive({});
const constantsDial = ref(false);
const constantsDialInner = ref(false);
const selectConstant = reactive({});

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
  updateConstant(selectConstant.value).then(res => {
    if ("ok" === res) {
      constantsDialInner.value = false;
      findConstants().then(res => {
        constants.value = res;
      })
      nfc('Success', 'constant update success');
    }
  });
}

const detailCode = ref('');

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
        detailCode.value = searchObj.code;
        searchVisible.value = false;
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

function handleColumnClick(tsCode) {
  detailCode.value = tsCode;
}
</script>

<template>
  <el-aside width="60px">
    <el-button type="info" size="large" class="big-btn"
               @click="toggleDark()" :icon="SwitchButton"></el-button>
    <el-button type="info" size="large" class="big-btn"
               @click="reloadPage()" :icon="Refresh"></el-button>

    <el-button type="info" @click="changeLineType" size="large"
               :icon="props.linetype?'Flag':'TrendCharts'"></el-button>


    <el-button type="info" size="large" class="big-btn"
               @click="showConstantsDial" :icon="Edit"></el-button>


    <el-button type="info" size="large"
               @click="showOtherConcernDial" :icon="Compass"></el-button>
    <el-button type="info" size="large"
               @click="showCurccDial" :icon="Histogram"></el-button>
    <el-button type="info" size="large"
               @click="showSearchDial" :icon="Search"></el-button>
  </el-aside>

  <el-dialog v-model="curCountVisible" title="CUR-COUNT"
             :show-close="false" center draggable destroy-on-close width="1000">
    <el-radio-group class="radio-group" v-model="curType" size="large">
      <el-radio-button label="3" value="3"/>
      <el-radio-button label="6" value="6"/>
      <el-radio-button label="0" value="0"/>
    </el-radio-group>
    <el-table :data="curCountTableData.value" class="mt-2" max-height="400px"
              :cell-style="cellStyle" stripe
              :header-cell-style="cellStyle"
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
              :cell-style="cellStyle" stripe
              :header-cell-style="cellStyle"
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
              :cell-style="cellStyle" stripe
              :header-cell-style="cellStyle"
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
    <el-radio-group class="radio-group mb-2" v-model="searchObj.type" size="large">
      <el-radio-button label="code" value="1"/>
      <el-radio-button label="name" value="2"/>
<!--      <el-radio-button label="limit" value="3"/>-->
<!--      <el-radio-button label="ophc" value="4"/>-->
<!--      <el-radio-button label="roc" value="5"/>-->
    </el-radio-group>
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
          <el-button type="info" round
                     @click="handleColumnClick(scope.row.tsCode)" :icon="List"></el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>

  <el-dialog v-model="sOpenVOVisible" title="NAME-SEARCH" :show-close="false" draggable destroy-on-close
             width="1000">
    <el-table :data="sOpenVOTableData.value.openVOList"
              :cell-style="cellStyle" max-height="600"
              :header-cell-style="cellStyle">
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
              :cell-style="cellStyle" max-height="400" stripe
              :header-cell-style="cellStyle">
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

  <el-dialog v-model="constantsDial" title="CONSTANT"
             :show-close="false" center draggable destroy-on-close width="1000">
    <el-table :data="constants.value" class="mt-2" max-height="400px"
              :cell-style="cellStyle"
              :header-cell-style="cellStyle"
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
          <el-input v-model="selectConstant.value.multiValueStr" :autosize="{ minRows: 2, maxRows: 4 }"
                    type="textarea">
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

  <DetailDialog v-model:code="detailCode"/>
</template>

<style scoped>

</style>