<script setup>

import {onMounted} from 'vue'
import ModelPage from '@/components/ModelPage.vue'
import {findLastContinuous} from "@/api/backend";
import {extraTdKey, tableData} from "@/api/util";

const td = tableData();

function fetchContinuous() {
  findLastContinuous().then(res => {
    td.value = res;
  })
}

onMounted(() => {
  fetchContinuous();
})

</script>

<template>
  <ModelPage/>
</template>

<style scoped>

</style>