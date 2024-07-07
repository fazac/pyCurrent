<script setup>
import router from "@/router";
import {isEmpty} from "@/api/util";

const props = defineProps(['icon', 'url']);


function openPage(path) {
  if (isEmpty(path)) {
    router.push('/');
  } else {
    router.push(path);
  }
}

</script>

<template>
  <el-button type="info" size="large" class="big-btn"
             @click="openPage(props.url)"
             :icon="props.icon"></el-button>
</template>

<style scoped>

</style>