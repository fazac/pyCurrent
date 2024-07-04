<script setup>
import {isEmpty} from "@/api/util";

const filterHandler = (
    value,
    row,
    column
) => {
  const property = column['property'];
  if (isEmpty(row[property])) {
    return false;
  }
  return value > 0 ? row[property].includes('预盈预增') : value < 0 ? row[property].includes('预亏预减') > 0 : !row[property].includes('预盈预增') && !row[property].includes('预亏预减');
}
</script>

<template>
  <el-table-column property="labels" min-width="110px" show-overflow-tooltip label="label"
                   :filters="[
                      { text: '预盈', value: '1' },
                      { text: '无', value: '0' },
                      { text: '预亏', value: '-1' },
                    ]"
                   :filter-method="filterHandler" filter-placement="bottom-end"/>
</template>

<style scoped>

</style>