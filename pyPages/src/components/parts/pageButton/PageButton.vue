<script setup>
import router from "@/router";
import {isEmpty} from "@/api/util";

const props = defineProps(['icon', 'url', 'badgeValue', 'badgePosition']);


function openPage(path) {
  if (isEmpty(path)) {
    router.push('/');
  } else {
    router.push(path);
  }
}

</script>

<template>
  <el-badge :value="badgeValue" v-if="badgeValue" type="info" :offset="isEmpty(props.badgePosition)?[-14,2]:[-43,0]"
            class="item fixed-width-badge">
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