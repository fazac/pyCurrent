<script setup>
import {onMounted, provide, reactive} from 'vue'
import axios from "@/api/http.js";
import ModelPage from "@/components/ModelPage.vue";

const codeDateList = reactive([{}]);
provide("myTableData", codeDateList);

onMounted(() => {
  if (!!window.EventSource) {
    const tabelSource = new EventSource(axios.defaults.baseURL + "/sse/createSSEConnect?clientId=&type=1");
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