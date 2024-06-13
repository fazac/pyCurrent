<script>
import {reactive} from 'vue'
import {v4 as uuidv4} from 'uuid'
import {findDetails} from '@/api/backend.js';

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
      this.source = new EventSource("http://localhost:19093/sse/createSSEConnect?clientId=" + uuidv4());
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
  <div class="container pb-5">
    <div class="row">
      <div class="col">
        <table class="table" aria-describedby="roc_result">
          <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">mark</th>
            <th scope="col">code</th>
            <th scope="col">rt</th>
            <th scope="col">h</th>
            <th scope="col">rr</th>
            <th scope="col">bp</th>
            <th scope="col">cp</th>
            <th scope="col">bar</th>
            <th scope="col">cm</th>
            <th scope="col">pe</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(itm,i) in codeDateList" :key="i">
            <!--          <th scope="row">{{ i + 1 }}</th>-->
            <!--          <td>{{ itm.remarks }}</td>-->
          </tr>
          </tbody>
        </table>
      </div>
      <div class="col">
        xxx
      </div>
    </div>

  </div>
</template>

<style scoped>

</style>