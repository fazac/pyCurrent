<script setup>
import {findCurcc} from "@/api/backend";
import {reactive, ref} from 'vue'
import {cellStyle} from "@/api/util";
import {Histogram,} from '@element-plus/icons-vue'


const curCountVisible = ref(false);
const curCountTableData = reactive({});
const curType = ref('');

function showCurccDial() {
  findCurcc().then(res => {
    curCountVisible.value = true;
    curCountTableData.value = res;
    curType.value = '3';
  })
}

</script>

<template>
  <el-button type="info" size="large"
             @click="showCurccDial" :icon="Histogram"></el-button>

  <el-dialog v-model="curCountVisible" title="CUR-COUNT"
             :show-close="false" center draggable destroy-on-close width="1000">
    <el-radio-group class="radio-group" v-model="curType" size="large">
      <el-radio-button label="3" value="3"/>
      <el-radio-button label="6" value="6"/>
      <el-radio-button label="0" value="0"/>
    </el-radio-group>
    <el-table :data="curCountTableData.value" class="mt-2" max-height="400px"
              :cell-style="cellStyle" stripe
              :header-cell-style="cellStyle"
              v-if="curType==='3'"
    >
      <el-table-column prop="c30a" label="a"/>
      <el-table-column prop="c30u" label="u"/>
      <el-table-column prop="c305u" label="5u"/>
      <el-table-column prop="c3035u" label="53u"/>
      <el-table-column prop="c3013u" label="31u"/>
      <el-table-column prop="c3001u" label="10u"/>
      <el-table-column prop="c3001d" label="01d"/>
      <el-table-column prop="c3013d" label="13d"/>
      <el-table-column prop="c3037d" label="37d"/>
      <el-table-column prop="c307d" label="7d"/>
    </el-table>
    <el-table :data="curCountTableData.value" class="mt-2" max-height="400px"
              :cell-style="cellStyle" stripe
              :header-cell-style="cellStyle"
              v-if="curType==='6'"
    >
      <el-table-column prop="c60a" label="a"/>
      <el-table-column prop="c60u" label="u"/>
      <el-table-column prop="c605u" label="5u"/>
      <el-table-column prop="c6035u" label="53u"/>
      <el-table-column prop="c6013u" label="31u"/>
      <el-table-column prop="c6001u" label="10u"/>
      <el-table-column prop="c6001d" label="01d"/>
      <el-table-column prop="c6013d" label="13d"/>
      <el-table-column prop="c6037d" label="37d"/>
      <el-table-column prop="c607d" label="7d"/>
    </el-table>
    <el-table :data="curCountTableData.value" class="mt-2" max-height="400px"
              :cell-style="cellStyle" stripe
              :header-cell-style="cellStyle"
              v-if="curType==='0'"
    >
      <el-table-column prop="c00a" label="a"/>
      <el-table-column prop="c00u" label="u"/>
      <el-table-column prop="c005u" label="5u"/>
      <el-table-column prop="c0035u" label="53u"/>
      <el-table-column prop="c0013u" label="31u"/>
      <el-table-column prop="c0001u" label="10u"/>
      <el-table-column prop="c0001d" label="01d"/>
      <el-table-column prop="c0013d" label="13d"/>
      <el-table-column prop="c0037d" label="37d"/>
      <el-table-column prop="c007d" label="7d"/>

    </el-table>
  </el-dialog>
</template>

<style scoped>

</style>