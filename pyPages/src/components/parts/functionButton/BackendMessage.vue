<script setup>
import {baseUrl, nfc} from "@/api/util";

const msgSource = new EventSource(baseUrl + "/sse/createSSEConnect?clientId=&type=3");
msgSource.addEventListener('open', function () {
  console.log("建立msg连接");
})
msgSource.onmessage = function (event) {
  if (event.lastEventId !== 'sse_client_id') {
    let msg = JSON.parse(event.data);
    nfc(msg[0], msg[1])
    console.log(event.data)
    window.electronAPI.sendMsg(msg[0], msg[1]);
  }
}
msgSource.onerror = function () {
  if (!!msgSource) {
    msgSource.close();
  }
};
</script>

<template>

</template>

<style scoped>

</style>