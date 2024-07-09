<script setup>
import router from "@/router";
import {isEmpty} from "@/api/util";

const props = defineProps(['icon', 'url', 'badgeValue']);


function openPage(path) {
  if (isEmpty(path)) {
    router.push('/');
  } else {
    router.push(path);
  }
}

</script>

<template>
  <el-badge :value="badgeValue" v-if="badgeValue" type="info" :offset="[-50,10]" class="item">
    <el-button type="info" size="large" class="big-btn"
               @click="openPage(props.url)"
               :icon="props.icon">
    </el-button>
  </el-badge>
  <el-button type="info" size="large" v-if="!badgeValue" class="big-btn"
             @click="openPage(props.url)"
             :icon="props.icon">
  </el-button>
</template>

<style scoped>

</style>