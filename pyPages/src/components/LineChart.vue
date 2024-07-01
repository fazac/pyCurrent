<script setup>
import DNLineChart from "@/components/LineChartPart/DNLineChart.vue";
import RTLineChart from "@/components/LineChartPart/RTLineChart.vue";

const props = defineProps(['code', 'dnshow']);



</script>

<template>
  <DNLineChart :code=props.code v-if="props.dnshow"></DNLineChart>
  <RTLineChart :code=props.code v-if="!props.dnshow"></RTLineChart>
</template>

<style scoped>

</style>