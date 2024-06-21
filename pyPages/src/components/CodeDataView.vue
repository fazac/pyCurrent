<script setup>
import {reactive, ref, onMounted} from 'vue'
import {ElTable} from 'element-plus'
import DNLineChart from './DNLineChart.vue'
import RTLineChart from './RTLineChart.vue'


const codeDateList = reactive([{}]);

const code = ref('');

const lineType = ref(true);

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
    <el-switch v-model="lineType" size="large" inline-prompt active-text="dn" inactive-text="rt" class="type-switch"
               style="--el-switch-on-color:  #006699; --el-switch-off-color: #47476b"></el-switch>
    <el-table
        @current-change="handleCurrentChange"
        :data="codeDateList.value" empty-text=" "
        highlight-current-row
        border
        stripe
        class="txtc main-table"
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
  <DNLineChart :code=code v-if="lineType"></DNLineChart>
  <RTLineChart :code=code v-if="!lineType"></RTLineChart>
</template>

<style scoped>

</style>