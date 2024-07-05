<script setup>
import {inject} from 'vue';
import DNLineChart from "@/components/LineChartPart/DNLineChart.vue";
import RTLineChart from "@/components/LineChartPart/RTLineChart.vue";
import CountSummaryLineChart from "@/components/LineChartPart/CountSummaryLineChart.vue";

const props = defineProps(['code', 'dnshow']);
const ldata = inject('ldata', null)

</script>

<template>
  <DNLineChart :code=props.code v-if="props.dnshow && props.code"></DNLineChart>
  <RTLineChart :code=props.code v-if="!props.dnshow  && props.code"></RTLineChart>
  <CountSummaryLineChart v-if="ldata"/>
</template>

<style scoped>

</style>