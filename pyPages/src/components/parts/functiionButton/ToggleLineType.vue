<script setup>
const props = defineProps(['linetype', 'code'])
const emit = defineEmits(['update:linetype', 'update:code'])

function changeLineType() {
  emit('update:linetype', !props.linetype)
  emit('update:code', '')
}

</script>

<template>
  <el-button type="info" @click="changeLineType" size="large"
             :icon="props.linetype?'Flag':'TrendCharts'"></el-button>
</template>

<style scoped>

</style>