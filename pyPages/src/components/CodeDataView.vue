<script setup>
import {reactive, ref, onMount} from 'vue'
import {v4 as uuidv4} from 'uuid'
import {findDetails} from '@/api/backend.js';

defineProps({
  codeDateList: {type: Array, required: true},
})

onMount()
{
  if (!!window.EventSource) {
    this.source = new EventSource("http://localhost:19093/sse/createSSEConnect?clientId=" + uuidv4());
    this.source.addEventListener('open', function (e) {
      console.log("建立连接");
    })
    this.source.onmessage = function (event) {
      if (event.lastEventId !== 'sse_client_id') {
        this.codeDateList = JSON.parse(event.data);
        console.log(this.codeDateList);
      }
    }
  }
}
,
}
</script>

<template>
  <div class="container pb-5">
    <div class="row">
      <div class="col">
        <el-table :data="codeDateList" style="width: 100%">
          <el-table-column prop="mark" label="mark" width="180"/>
        </el-table>
      </div>
      <div class="col">
        xxx
      </div>
    </div>

  </div>
</template>

<style scoped>

</style>