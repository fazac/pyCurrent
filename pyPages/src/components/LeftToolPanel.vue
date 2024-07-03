<script setup>
import {onMounted, ref} from 'vue'

import {BellFilled, Compass, DataBoard, Film, HomeFilled, Link, TopRight} from '@element-plus/icons-vue'
import router from "@/router";
import axios from "@/api/http";
import {nfc} from "@/api/util";

const home_path = ref(true);

const props = defineProps(['linetype', 'code'])
const emit = defineEmits(['update:linetype', 'update:code'])

function changeLineType() {
  emit('update:linetype', !props.linetype)
  emit('update:code', '')
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
  const msgSource = new EventSource(axios.defaults.baseURL + "/sse/createSSEConnect?clientId=&type=3");
  msgSource.addEventListener('open', function () {
    console.log("建立msg连接");
  })
  msgSource.onmessage = function (event) {
    if (event.lastEventId !== 'sse_client_id') {
      let msgArr = JSON.parse(event.data);
      nfc(msgArr[0], msgArr[1]);
      window.electronAPI.sendMsg(msgArr[0], msgArr[1]);
    }
  }
  msgSource.onerror = function () {
    if (!!msgSource) {
      msgSource.close();
    }
  };
})

</script>

<template>
  <el-aside width="60px">

    <el-button type="info" @click="changeLineType" size="large"
               :icon="props.linetype?'Flag':'TrendCharts'"></el-button>

    <el-button type="info" size="large" class="big-btn"
               @click="openPage" v-if="!home_path"
               :icon="HomeFilled"></el-button>

    <el-button type="info" size="large" class="big-btn"
               @click="openPage('board')" v-if="home_path"
               :icon="DataBoard"></el-button>

    <el-button type="info" size="large" class="big-btn"
               @click="openPage('other')" v-if="home_path"
               :icon="Compass"></el-button>

    <el-button type="info" size="large" class="big-btn"
               @click="openPage('ophc')" v-if="home_path"
               :icon="BellFilled"></el-button>

    <el-button type="info" size="large" class="big-btn"
               @click="openPage('roc')" v-if="home_path"
               :icon="Film"></el-button>

    <el-button type="info" size="large" class="big-btn"
               @click="openPage('all')" v-if="home_path"
               :icon="Link"></el-button>

    <el-button type="info" size="large" class="big-btn"
               @click="openPage('continuous')" v-if="home_path"
               :icon="TopRight"></el-button>
  </el-aside>
</template>

<style scoped>

</style>