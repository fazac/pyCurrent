<script setup>

</script>

<template>
  <el-table-column prop="tradeDate" label="date">
    <template #default="scope">
      <span>{{ scope.row.tradeDate.substring(4, 10).replaceAll('-', '') }}</span>
    </template>
  </el-table-column>
</template>

<style scoped>

</style>