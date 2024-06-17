<script setup>
import echarts from '@/echarts';
import {onMounted} from 'vue';

const props = defineProps({
  rtdata: Array
})

onMounted(() => {
  setTimeout(() => {
    const myChart = echarts.init(document.getElementById('mainlc'));
    let option = {
      title: {},
      dataset: {
        dimensions: ['cp', 'h', 'rt', 'bar', 'cm', 'pe', 'vol'],
        source: this.rtdata,
      },
      tooltip: {},
      legend: {
        data: ['p']
      },
      xAxis: {
        type: "category",
        data: [...this.rtdata.keys()]
      },
      yAxis: {},
      series: [
        {
          name: 'p',
          type: 'line',
          encode: {
            y: cp,
          }
        }
      ]
    };
    myChart.setOption(option);
  })
})

</script>

<template>
  <div id="mainlc" style="width: 600px;height:400px;"></div>
</template>

<style scoped>

</style>