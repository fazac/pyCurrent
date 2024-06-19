<script setup>
import {reactive, ref, onMounted} from 'vue'
import {ElTable} from 'element-plus'
import LineChart from '../components/LineChart.vue'


const codeDateList = reactive([{}]);

const code = ref('');

const handleCurrentChange = (val) => {
  if (val !== null) {
    code.value = val.tsCode;
  }
}

onMounted(() => {
  if (!!window.EventSource) {
    window.source = new EventSource("http://localhost:19093/sse/createSSEConnect?clientId=");
    window.source.addEventListener('open', function (e) {
      console.log("建立连接");
    })
    window.source.onmessage = function (event) {
      if (event.lastEventId !== 'sse_client_id') {
        codeDateList.value = JSON.parse(event.data);
      }
    }
  }

});


</script>

<template>
  <div class="table-container">
    <el-table
        @current-change="handleCurrentChange"
        :data="codeDateList.value" empty-text=" "
        highlight-current-row
        border
        stripe
        style="width: 100%;text-align: center;max-width: 1200px"
    >
      <el-table-column prop="mark" label="mark" class="txtr"/>
      <el-table-column prop="cp" label="cp" class="txtr"/>
      <el-table-column prop="rt" label="rt" class="txtr"/>
      <el-table-column prop="h" label="h" class="txtr"/>
      <el-table-column prop="bp" label="bp" class="txtr"/>
      <el-table-column prop="rr" label="rr" class="txtr"/>
      <el-table-column prop="cm" label="cm" class="txtr"/>
      <el-table-column prop="pe" label="pe" class="txtr"/>
      <el-table-column prop="tsCode" label="code" class="txtr">
        <template #default="scope">
          <span>{{ scope.row.tsCode.substring(2, 6) }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <LineChart :code=code>

  </LineChart>

</template>

<style scoped>

</style>