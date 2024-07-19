<script setup>
import {inject, reactive, ref, useSlots, watch} from 'vue';
import {
  cellStyle,
  extraMarkShow,
  extraPchShow,
  extraTdKey,
  headerCellStyle,
  isEmpty,
  joinArr,
  rowStyleClass
} from "@/api/util";
import CmColumn from "@/components/TablePart/CmColumn.vue";
import {default as vElTableInfiniteScroll} from "el-table-infinite-scroll";
import {List, TrendCharts, Cherry} from '@element-plus/icons-vue'

const props = defineProps(['elseColumn'])

const myTableData = inject('myTableData', null);
const myTableHeight = inject('myTableHeight', 400);
const codeDisplay = inject('codeDisplay', true);
const slotColumn = !!useSlots().columnSlot && props.elseColumn;

const pageIndex = ref(0);
const displayData = reactive({});
const tableFullHeight = ref('200px');
const filterColumns = reactive({});
const handleData = ref([]);

const loadMore = () => {
  if (!isEmpty(handleData.value)) {
    if (pageIndex.value < handleData.value.length) {
      pageIndex.value += 10;
      displayData.value = handleData.value.slice(0, handleData.value.length <= pageIndex.value ? handleData.value.length : pageIndex.value)
      let tmpHeight = displayData.value.length * 49 + 40;
      tableFullHeight.value = (tmpHeight < myTableHeight.value ? tmpHeight : myTableHeight) + 'px';
    } else {
      displayData.value = handleData.value;
      let tmpHeight = handleData.value.length * 49 + 40;
      tableFullHeight.value = (tmpHeight < myTableHeight.value ? tmpHeight : myTableHeight) + 'px';
    }
  }
}

const handleFilterChange = (filters) => {
  if (!isEmpty(filters)) {
    Object.entries(filters).map(entry => {
      if (isEmpty(filterColumns.value)) {
        filterColumns.value = {};
      }
      filterColumns.value[entry[0]] = entry[1];
    })
  }
  handleData.value = myTableData.value;
  if (!isEmpty(filterColumns.value)) {
    Object.entries(filterColumns.value).map(entry => {
      if (entry[0] === 'extraNode.mark') {
        if (entry[1].length > 0) {
          switch (entry[1][0]) {
            case '!R':
              handleData.value = joinArr(handleData.value, myTableData.value.filter(row => {
                return !row.extraNode.mark.startsWith('R')
              }));
              break;
            case 'R':
              handleData.value = joinArr(handleData.value, myTableData.value.filter(row => {
                return row.extraNode.mark.includes('R');
              }));
              break;
            case 'A':
              handleData.value = joinArr(handleData.value, myTableData.value.filter(row => {
                return row.extraNode.mark.includes('A');
              }));
              break;
            case 'L':
              handleData.value = joinArr(handleData.value, myTableData.value.filter(row => {
                return row.extraNode.mark.includes('L');
              }));
              break;
            case 'F':
              handleData.value = joinArr(handleData.value, myTableData.value.filter(row => {
                return row.extraNode.mark.includes('F');
              }));
              break;
          }
        }
      } else if (entry[0] === 'pe') {
        if (entry[1].length > 0) {
          if (entry[1][0] > 0) {
            handleData.value = joinArr(handleData.value, myTableData.value.filter(row => {
              return row.pe > 0;
            }));
          } else if (entry[1][0] < 0) {
            handleData.value = joinArr(handleData.value, myTableData.value.filter(row => {
              return row.pe < 0;
            }));
          }
        }
      } else if (entry[0] === 'labels') {
        if (entry[1].length > 0) {
          let handleDataFirst = [];
          if (entry[1][0] > 0) {
            handleDataFirst = myTableData.value.filter(row => {
              if (isEmpty(row.labels)) {
                return false;
              }
              return row.labels.includes('预盈预增');
            });
          } else if (entry[1][0] < 0) {
            handleDataFirst = myTableData.value.filter(row => {
              if (isEmpty(row.labels)) {
                return false;
              }
              return row.labels.includes('预亏预减');
            });
          } else {
            handleDataFirst = myTableData.value.filter(row => {
              if (isEmpty(row.labels)) {
                return false;
              }
              return !row.labels.includes('预盈预增') && !row.labels.includes('预亏预减');
            });
          }
          if (entry[1].length > 1) {
            let handleDataElse = [];
            if (entry[1][1] > 0) {
              handleDataElse = myTableData.value.filter(row => {
                if (isEmpty(row.labels)) {
                  return false;
                }
                return row.labels.includes('预盈预增');
              });
            } else if (entry[1][1] < 0) {
              handleDataElse = myTableData.value.filter(row => {
                if (isEmpty(row.labels)) {
                  return false;
                }
                return row.labels.includes('预亏预减');
              });
            } else {
              handleDataElse = myTableData.value.filter(row => {
                if (isEmpty(row.labels)) {
                  return false;
                }
                return !row.labels.includes('预盈预增') && !row.labels.includes('预亏预减');
              });
            }
            if (!isEmpty(handleDataElse)) {
              handleDataFirst = handleDataFirst.concat(handleDataElse);
              let newArr = new Set(handleDataFirst);
              handleDataFirst = Array.from(newArr);
            }
          }
          handleData.value = joinArr(handleData.value, handleDataFirst);
        }
      } else if (entry[0] === 'code') {
        if (entry[1].length > 0) {
          let handleDataFirst = [];
          if (entry[1][0] === '3') {
            handleDataFirst = myTableData.value.filter(row => {
              return row.code.startsWith('3');
            });
          } else if (entry[1][0] === '6') {
            handleDataFirst = myTableData.value.filter(row => {
              return row.code.startsWith('6');
            });
          } else {
            handleDataFirst = myTableData.value.filter(row => {
              return row.code.startsWith('0');
            });
          }
          if (entry[1].length > 1) {
            let handleDataElse = [];
            if (entry[1][1] === '3') {
              handleDataElse = myTableData.value.filter(row => {
                return row.code.startsWith('3');
              });
            } else if (entry[1][1] === '6') {
              handleDataElse = myTableData.value.filter(row => {
                return row.code.startsWith('6');
              });
            } else {
              handleDataElse = myTableData.value.filter(row => {
                return row.code.startsWith('0');
              });
            }
            if (!isEmpty(handleDataElse)) {
              handleDataFirst = handleDataFirst.concat(handleDataElse);
              let newArr = new Set(handleDataFirst);
              handleDataFirst = Array.from(newArr);
            }
          }
          handleData.value = joinArr(handleData.value, handleDataFirst);
        }
      }
    })
  }
  pageIndex.value = 0;
  loadMore();
};

function handleSortChange(column) {
  console.log(column);
  let sp = column.prop;
  let spa = [];
  if (sp.includes('.')) {
    spa = sp.split('.');
  }
  if (column.order === "descending") {
    myTableData.value = myTableData.value.sort(function (a, b) {
      if (spa.length > 0) {
        return b[spa[0]][spa[1]] - a[spa[0]][spa[1]]
      } else {
        return b[sp] - a[sp]
      }
    })
  } else if (column.order === "ascending") {
    myTableData.value = myTableData.value.sort(function (a, b) {
      if (spa.length > 0) {
        return a[spa[0]][spa[1]] - b[spa[0]][spa[1]]
      } else {
        return a[sp] - b[sp]
      }
    })
  }
  handleFilterChange();
}

function reloadData() {
  setTimeout(() => {
    if (!isEmpty(myTableData) && !isEmpty(myTableData.value)) {
      handleData.value = myTableData.value;
    }
    handleFilterChange();
  })
}

watch(myTableData, () => {
  reloadData();
})


const lineCode = inject('lineCode', null);
const detailCode = inject('detailCode', null);
const peFilterFlag = ref(false);

function showDetailDial(tsCode) {
  detailCode.value = tsCode;
}

function showChartLine(tsCode) {
  lineCode.value = tsCode;
}

function addSpFilter() {
  peFilterFlag.value = !peFilterFlag.value;
  if (peFilterFlag.value) {
    if (isEmpty(filterColumns.value)) {
      filterColumns.value = {};
    }
    filterColumns.value['pe'] = ['1'];
    filterColumns.value['labels'] = ['1', '0'];
    myTableData.value = myTableData.value.sort(function (a, b) {
      return a['pe'] - b['pe'];
    })
    handleFilterChange();
  } else {
    window.location.reload();
  }
}

</script>

<template>
  <el-table
      v-el-table-infinite-scroll='loadMore'
      :data="displayData.value"
      v-if="myTableData"
      height="tableFullHeight.value"
      empty-text=" no data "
      :max-height="myTableHeight"
      class="mt-1p"
      border
      stripe
      :row-class-name="rowStyleClass"
      :cell-style="cellStyle"
      :header-cell-style="headerCellStyle"
      @filter-change="handleFilterChange"
      @sort-change="handleSortChange"
  >
    <slot name="columnSlot"/>
    <el-table-column prop="extraNode.mark" label="mark"
                     v-if="extraMarkShow(myTableData)"
                     :filter-multiple="false" :filters="[
        { text: 'F', value: 'F' },
        { text: '!R', value: '!R' },
        { text: 'L', value: 'L' },
        { text: 'R', value: 'R' },
        { text: 'A', value: 'A' },
      ]" filter-placement="bottom-end" column-key="extraNode.mark"/>
    <el-table-column :sortable="item.sortable"
                     v-if="!slotColumn && !isEmpty(myTableData)"
                     v-for="item in extraTdKey(myTableData)"
                     :key="item.fullProp"
                     :label="item.prop"
                     :prop="item.fullProp">
    </el-table-column>
    <el-table-column property="pch" v-if="!extraPchShow(myTableData)" sortable label="pch"/>
    <el-table-column property="currentPri" sortable label="cp"/>
    <CmColumn/>
    <el-table-column prop="pe" label="pe" :filter-multiple="false" sortable :filters="[
        { text: '正', value: '1' },
        { text: '负', value: '-1' },
      ]" filter-placement="bottom-end" column-key="pe"/>
    <el-table-column property="pb" sortable label="pb"/>
    <el-table-column property="code" :filters="[
                      { text: '3', value: '3' },
                      { text: '6', value: '6' },
                      { text: '0', value: '0' },
                    ]"
                     filter-placement="bottom-end" column-key="code" sortable label="code" v-if="!codeDisplay"/>
    <el-table-column property="labels" v-if="!codeDisplay" min-width="110px" show-overflow-tooltip label="label"
                     :filters="[
                      { text: '预盈', value: '1' },
                      { text: '无', value: '0' },
                      { text: '预亏', value: '-1' },
                    ]"
                     filter-placement="bottom-end" column-key="labels"/>
    <el-table-column label="详情" width="130">
      <template #header>
        <el-button type="info" size="small"
                   @click="addSpFilter()"
                   :icon="Cherry">
        </el-button>
      </template>
      <template #default="scope">
        <el-button type="info" round
                   @click="showDetailDial(scope.row.code)"
                   :icon="List"></el-button>
        <el-button type="info" round
                   @click="showChartLine(scope.row.code)"
                   :icon="TrendCharts"></el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<style scoped>

</style>