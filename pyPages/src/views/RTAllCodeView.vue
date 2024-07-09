<script setup>
import {onMounted} from 'vue'
import ModelPage from '@/components/ModelPage.vue'
import {cellStyle, headerCellStyle, tableData} from "@/api/util";
import {findLast} from "@/api/backend";

const rtData = tableData();

function fetchRtAll() {
  findLast().then(res => {
    rtData.value = res;
  })
}

onMounted(() => {
  fetchRtAll();
})
</script>

<template>
  <ModelPage/>
</template>

<style scoped>

</style>