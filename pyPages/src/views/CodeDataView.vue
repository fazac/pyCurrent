<script setup>
import {onMounted} from 'vue'
import {baseUrl, tableData} from "@/api/util";
import ModelPage from "@/components/ModelPage.vue";

const codeDateList = tableData();

onMounted(() => {
  if (!!window.EventSource) {
    const tabelSource = new EventSource(baseUrl + "/sse/createSSEConnect?clientId=&type=1");
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

  }

});


</script>

<template>
  <ModelPage/>
</template>

<style scoped>
</style>