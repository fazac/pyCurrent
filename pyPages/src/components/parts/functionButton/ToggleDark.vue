<script setup>
import {SwitchButton,} from '@element-plus/icons-vue'
import {useDark, useToggle} from '@vueuse/core'

const isDark = useDark()
const toggleDark = useToggle(isDark)
</script>

<template>
  <el-button type="info" size="large" class="big-btn"
             @click="toggleDark()" :icon="SwitchButton"></el-button>
</template>

<style scoped>

</style>