<script setup>
import echarts from '@/echarts';
import {reactive, ref, watch} from 'vue';
import {findDataByCode} from '@/api/backend.js'


const props = defineProps(['code'])

const rtHisData = reactive([{}]);
const dnHisData = reactive([{}]);
const dnPriMin = ref(0);
const dnPriMax = ref(0);


watch(() => props.code, async (newCode, oldCode) => {
  if (newCode !== undefined && newCode !== null) {
    prepareDnHisData();
    prepareRTHisData();

    // createLine();
  }
});

function prepareDnHisData() {
  findDataByCode(props.code).then(res => {
    dnHisData.value = res.dnData;
    dnPriMin.value = res.dnPriMin;
    dnPriMax.value = res.dnPriMax;
    createDnLine();
  })
}

function prepareRTHisData() {
  const source = new EventSource("http://localhost:19093/sse/createSSEConnect?clientId=" + props.code);
  source.onmessage = function (event) {
    rtHisData.value = event.data;
  }
}

//
// function createLine() {
//   setTimeout(() => {
//     if (rtHisData.value !== undefined) {
//       const myChart = echarts.init(document.getElementById('mainlc'));
//       let option = {
//         title: {},
//         dataset: {
//           dimensions: ['di', 'cp', 'h', 'rt', 'bar', 'cm', 'pe', 'vol'],
//           source: rtHisData.value,
//         },
//         tooltip: {},
//         legend: {
//           data: ['p']
//         },
//         xAxis: {type: 'category'},
//         yAxis: {},
//         series: [
//           {
//             name: 'p',
//             type: 'line',
//             encode: {y: 'cp'},
//           }
//         ]
//       };
//       myChart.setOption(option);
//     }
//   })
// }

function createDnLine() {
  setTimeout(() => {
    const myChart = echarts.init(document.getElementById('mainld'));
    let option = {
      dataset: {
        dimensions: ['di', 'cp', 'hp', 'lp', 'ap', 'h', 'vol'],
        source: dnHisData.value
      },
      legend: {
        type: 'plain',
        data: ['cp', 'hp', 'lp', 'ap', 'vol'],
        selected: {
          'cp': true,
          'hp': false,
          'lp': true,
          'ap': false,
          'vol': false,
        },
      },
      xAxis: {
        type: 'category',
        show: false
      },
      yAxis: [
        {type: 'value', position: 'left', scale: true, min: dnPriMin.value, max: dnPriMax.value},
        {type: 'value', position: 'left', scale: true, min: dnPriMin.value, max: dnPriMax.value},
        {type: 'value', position: 'left', scale: true, min: dnPriMin.value, max: dnPriMax.value},
        {type: 'value', position: 'left', scale: true, min: dnPriMin.value, max: dnPriMax.value},
        {type: 'value', position: 'right', scale: true},
      ],
      series: [
        {
          type: 'line',
          name: 'cp',
          encode: {y: 'cp'},
          yAxisIndex: 0,
          lineStyle: {
            color: 'blue'
          },
          label: {
            show: true,
          },
        },
        {
          name: 'hp',
          type: 'line',
          encode: {y: 'hp'},
          yAxisIndex: 1,
          lineStyle: {
            show: false,
          },
        },
        {
          name: 'lp',
          type: 'line',
          encode: {y: 'lp'},
          yAxisIndex: 2,
          label: {
            show: true,
          },
        },
        {
          name: 'ap',
          type: 'line',
          encode: {y: 'ap'},
          yAxisIndex: 3,
        },
        {
          name: 'vol',
          type: 'bar',
          encode: {y: 'vol'},
          yAxisIndex: 4,
        }
      ]
    };
    myChart.setOption(option);
  }, 0)
}

</script>

<template>
  <div class="table-container mt-20">
    <!--  <div id="mainlc" style="width: 600px;height:400px;"></div>-->
    <div id="mainld" style="width: 100%;height:400px;"></div>
  </div>
</template>

<style scoped>

</style>