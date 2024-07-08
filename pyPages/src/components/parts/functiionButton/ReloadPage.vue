<script setup>
import {Refresh,} from '@element-plus/icons-vue'

function reloadPage() {
  window.location.reload();
}

</script>

<template>
  <el-button type="info" size="large" class="big-btn"
             @click="reloadPage()" :icon="Refresh"></el-button>
</template>

<style scoped>

</style>