<script setup>
import {reactive, ref, onMounted} from 'vue'
import {useDark, useToggle} from '@vueuse/core'
import {ElTable} from 'element-plus'
import DNLineChart from './DNLineChart.vue'
import RTLineChart from './RTLineChart.vue'

const isDark = useDark()
const toggleDark = useToggle(isDark)

const isDarkType = ref(true);

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
    window.source = new EventSource("http://139.84.194.82:7800/sse/createSSEConnect?clientId=");
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

function changeLineType() {
  code.value = '';
}

</script>

<template>
  <div class="table-container">
    <span class="type-switch">
      <el-switch v-model="lineType" @change="changeLineType" size="large" inline-prompt active-text="dn"
                 inactive-text="rt"
                 style="--el-switch-on-color:  #006699; --el-switch-off-color: #47476b"></el-switch>
      <el-switch v-model="isDarkType" @change="toggleDark()" size="large" inline-prompt active-text="off"
                 inactive-text="on" class="ml-20"
                 style="--el-switch-on-color:  #006699; --el-switch-off-color: #47476b"></el-switch>
    </span>
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
.fixed-height-icon {
  height: 50px;
  line-height: 50px;
  width: auto;
  text-align: center;
}
</style>