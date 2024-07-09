<script setup>

</script>

<template>
  <el-table-column prop="cm" sortable  label="cm">
    <template #default="scope">
      <span>{{ (scope.row.cm / 100000000).toFixed(2) }}</span>
    </template>
  </el-table-column>
</template>

<style scoped>

</style>