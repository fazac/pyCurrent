<script setup>
import {reactive, ref, onMounted} from 'vue'
import {isEmpty, nfc, nullArr, txtCenter} from "@/api/util";
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

const sLimitVOVisible = ref(false);
const sLimitVOType = ref('');
const sLimitVOTableData = reactive({});

onMounted(() => {
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
})


</script>

<template>
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