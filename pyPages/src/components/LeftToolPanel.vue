<script setup>
import {onMounted, reactive, ref} from 'vue'
import {useDark, useToggle} from '@vueuse/core'
import {DataBoard, Edit, HomeFilled, Refresh, SwitchButton, Compass} from '@element-plus/icons-vue'
import {findConstants, updateConstant} from '@/api/backend.js'
import {nfc, txtCenter} from '@/api/util'
import router from "@/router";

const home_path = ref(true);

const isDark = useDark()
const toggleDark = useToggle(isDark)

const constants = reactive({});
const constantsDial = ref(false);
const constantsDialInner = ref(false);
const selectConstant = reactive({});


function reloadPage() {
  window.location.reload();
}

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

function openPage(path) {
  // window.electronAPI.openUrl('/board')
  // window.open('board', '_blank');
  if (home_path.value) {
    router.push(path);
  } else {
    router.push('/');
  }

}

onMounted(() => {
  home_path.value = '/' === router.currentRoute.value.path;
})

</script>

<template>
  <el-aside width="60px">
    <el-button type="info" size="large" class="big-btn"
               @click="toggleDark()" :icon="SwitchButton"></el-button>
    <el-button type="info" size="large" class="big-btn"
               @click="reloadPage()" :icon="Refresh"></el-button>
    <el-button type="info" size="large" class="big-btn"
               @click="showConstantsDial" :icon="Edit"></el-button>

    <el-button type="info" size="large" class="big-btn"
               @click="openPage" v-if="!home_path"
               :icon="HomeFilled"></el-button>

    <el-button type="info" size="large" class="big-btn"
               @click="openPage('board')" v-if="home_path"
               :icon="DataBoard"></el-button>

    <el-button type="info" size="large" class="big-btn"
               @click="openPage('other')" v-if="home_path"
               :icon="Compass"></el-button>



    <el-dialog v-model="constantsDial" title="CONSTANT"
               :show-close="false" center draggable destroy-on-close width="1000">
      <el-table :data="constants.value" class="mt-2" max-height="400px"
                :cell-style="txtCenter"
                :header-cell-style="txtCenter"
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
  </el-aside>
</template>

<style scoped>

</style>