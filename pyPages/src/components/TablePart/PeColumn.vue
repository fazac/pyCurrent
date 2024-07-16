<script setup>

const filterHandler = (
    value,
    row,
    column
) => {
  const property = column['property'];
  return value > 0 ? row[property] > 0 : row[property] < 0;
}

</script>

<template>
  <el-table-column prop="pe" label="pe" :filter-multiple="false" sortable :filters="[
        { text: '正', value: '1' },
        { text: '负', value: '-1' },
      ]" :filter-method="filterHandler" filter-placement="bottom-end" column-key="pe"/>
</template>

<style scoped>

</style>