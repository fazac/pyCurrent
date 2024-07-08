<script setup>
import {onMounted, provide, reactive} from 'vue'
import ModelPage from '@/components/ModelPage.vue'
import {nullArr} from "@/api/util";
import {searchSome} from "@/api/backend";

const searchObj = reactive(
    {
      searchDate: null,
    }
);
const sLimitVOTableData = reactive({});

provide("myTableData", sLimitVOTableData);

function fetchLimit() {
  searchSome("3", ...nullArr(2), searchObj.searchDate != null ? searchObj.searchDate.getTime() : null).then(res => {
    sLimitVOTableData.value = res;
  })
}

onMounted(() => {
  fetchLimit();
})

</script>

<template>
  <ModelPage>
    <template #queryParams>
      <el-date-picker
          v-model="searchObj.searchDate"
          type="date"
          placeholder="Last Day Or Pick A Day"
          @change="fetchLimit"
      />
    </template>
    <template #elseColumn>
      <el-table-column property="count" sortable label="count"/>
    </template>
  </ModelPage>
</template>

<style scoped>

</style>