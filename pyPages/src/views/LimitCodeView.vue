<script setup>
import {onMounted, reactive} from 'vue'
import ModelPage from '@/components/ModelPage.vue'
import {nullArr, tableData} from "@/api/util";
import {searchSome} from "@/api/backend";

const searchObj = reactive(
    {
      searchDate: null,
    }
);
const sLimitVOTableData = tableData();

function fetchLimit() {
  searchSome("3", ...nullArr(2), searchObj.searchDate != null ? searchObj.searchDate.getTime() : null).then(res => {
    sLimitVOTableData.value = res.codeDataVOList;
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
  </ModelPage>
</template>

<style scoped>

</style>