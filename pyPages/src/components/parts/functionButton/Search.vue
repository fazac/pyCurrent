<script setup>
import {reactive, ref} from 'vue'
import {Search,} from '@element-plus/icons-vue'
import {cellStyle, isEmpty, nfc, nullArr} from "@/api/util";
import {searchSome} from "@/api/backend";

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

const sOpenVOVisible = ref(false);
const sOpenVOTableData = reactive({});

const sLimitVOVisible = ref(false);
const sLimitVOType = ref('');
const sLimitVOTableData = reactive({});

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

</script>

<template>
  <el-button type="info" size="large"
             @click="showSearchDial" :icon="Search"></el-button>

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

</template>

<style scoped>

</style>