<script setup>

const filterHandler = (
    value,
    row,
) => {
  switch (value) {
    case '!R':
      return !row.extraNode.mark.startsWith('R');
    case 'R':
      return row.extraNode.mark.includes('R');
    case 'A':
      return row.extraNode.mark.includes('A');
    case 'L':
      return row.extraNode.mark.includes('L');
    case 'F':
      return row.extraNode.mark.includes('F');
  }
}

</script>

<template>
  <el-table-column prop="extraNode.mark" label="mark" :filter-multiple="false" :filters="[
        { text: 'F', value: 'F' },
        { text: '!R', value: '!R' },
        { text: 'L', value: 'L' },
        { text: 'R', value: 'R' },
        { text: 'A', value: 'A' },
      ]" :filter-method="filterHandler" filter-placement="bottom-end" column-key="extraNode.mark"/>
</template>

<style scoped>

</style>