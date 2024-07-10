<script setup>
import {View,} from '@element-plus/icons-vue'
import {inject} from 'vue'

const codeDisplay = inject('codeDisplay', true);

function changecodeDisplay() {
  codeDisplay.value = !codeDisplay.value;
}
</script>

<template>
  <el-button type="info" @click="changecodeDisplay" size="large"
             :icon="View"></el-button>
</template>

<style scoped>

</style>