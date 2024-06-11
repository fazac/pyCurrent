<script setup>
import {reactive} from 'vue'
import {v4 as uuidv4} from 'uuid'

export default {
  name: "CodeDataView",
  methods: {},
  data() {
    return {
      codeDateList: {},
      source: {},
    }
  },
  created() {
    if (!!window.EventSource) {
      this.source = new EventSource("http://localhost:19092/sse/createSSEConnect?clientId=" + uuidv4());
      this.source.addEventListener('open', function (e) {
        console.log("建立连接");
      })
      this.source.onmessage = function (event) {
        console.log(event.data);
        console.log(event);
        this.codeDateList = reactive(event.data);
      }
    }
  },
}
</script>

<template>

</template>

<style scoped>

</style>