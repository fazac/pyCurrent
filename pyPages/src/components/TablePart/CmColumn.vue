<script setup>

</script>

<template>
  <el-table-column prop="totalAmount" min-width="100" label="tamount">
    <template #default="scope">
      <span>{{ (scope.row.totalAmount / 100000000).toFixed(2) }}</span>
    </template>
  </el-table-column>
</template>

<style scoped>

</style>