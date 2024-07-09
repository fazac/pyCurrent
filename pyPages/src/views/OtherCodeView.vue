<script setup>
import {onMounted} from 'vue'
import {findOtherConcernList} from "@/api/backend";
import ModelPage from "@/components/ModelPage.vue";
import {tableData} from "@/api/util";

const otherConcernTableData = tableData();

function fetchOther() {
  findOtherConcernList().then(res => {
    otherConcernTableData.value = res;
  })
}

onMounted(() => {
  fetchOther();
})

</script>

<template>
  <ModelPage/>
</template>

<style scoped>

</style>