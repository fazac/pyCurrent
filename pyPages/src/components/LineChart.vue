<script setup>
import {inject} from 'vue';
import DNLineChart from "@/components/LineChartPart/DNLineChart.vue";
import RTLineChart from "@/components/LineChartPart/RTLineChart.vue";
import CSLineChart from "@/components/LineChartPart/CSLineChart.vue";
import HPLineChart from "@/components/LineChartPart/HPLineChart.vue";

const lineType = inject('lineType', null)

</script>

<template>
  <DNLineChart v-if="lineType === 'dn'"/>
  <RTLineChart v-if="lineType === 'rt'"/>
  <HPLineChart v-if="lineType === 'hp'"/>
</template>

<style scoped>

</style>