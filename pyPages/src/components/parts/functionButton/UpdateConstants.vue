<script setup>
import {reactive, ref} from 'vue'
import {findConstants, updateConstant} from "@/api/backend";
import {cellStyle, nfc} from "@/api/util";
import {Edit,} from '@element-plus/icons-vue'

const constants = reactive({});
const constantsDial = ref(false);
const constantsDialInner = ref(false);
const selectConstant = reactive({});

function showConstantsDial() {
  findConstants().then(res => {
    constants.value = res;
    constantsDial.value = true;
  })
}


function handelSelectConstantRow(row) {
  selectConstant.value = row;
  if (selectConstant.value.multiValue !== null) {
    selectConstant.value.multiValueStr = JSON.stringify(selectConstant.value.multiValue);
  }
  constantsDialInner.value = true;
}

function onSubmit() {
  updateConstant(selectConstant.value).then(res => {
    if ("ok" === res) {
      constantsDialInner.value = false;
      findConstants().then(res => {
        constants.value = res;
      })
      nfc('Success', 'constant update success');
    }
  });
}


</script>

<template>
  <el-button type="info" size="large" class="big-btn"
             @click="showConstantsDial" :icon="Edit"></el-button>

  <el-dialog v-model="constantsDial" title="CONSTANT"
             :show-close="false" center draggable destroy-on-close width="1000">
    <el-table :data="constants.value" class="mt-2" max-height="400px"
              :cell-style="cellStyle"
              :header-cell-style="cellStyle"
              @row-click="handelSelectConstantRow"
    >
      <el-table-column property="ckey" label="ckey"/>
      <el-table-column property="cvalue" label="cvalue"/>
      <el-table-column prop="mv" show-overflow-tooltip label="multiValue">
        <template #default="scope">
          <span>{{ scope.row.multiValue !== null ? JSON.stringify(scope.row.multiValue) : '' }}</span>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="constantsDialInner" align-center center destroy-on-close
               :show-close="false" width="600">
      <el-form :model="selectConstant">
        <el-form-item label="cval" label-width="88px">
          <el-input v-model="selectConstant.value.cvalue" autocomplete="off" type="textarea"/>
        </el-form-item>
        <el-form-item label="mval" label-width="88px">
          <el-input v-model="selectConstant.value.multiValueStr" :autosize="{ minRows: 2, maxRows: 4 }"
                    type="textarea">
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="constantsDialInner = false">Cancel</el-button>
          <el-button type="primary" @click="onSubmit">
            Confirm
          </el-button>
        </div>
      </template>
    </el-dialog>
  </el-dialog>

</template>

<style scoped>

</style>