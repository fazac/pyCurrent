<script setup>
import echarts from '@/echarts';
import {reactive, ref, watch} from 'vue';
import {findDataByCode} from '@/api/backend.js'


const props = defineProps(['code'])

const dnHisData = reactive([{}]);
const dnPriMin = ref(0);
const dnPriMax = ref(0);
const dnChart = reactive({});


watch(() => props.code, async (newCode, oldCode) => {
  if (newCode !== undefined && newCode !== null && newCode !== oldCode) {
    prepareDnHisData();
  }
});

function prepareDnHisData() {
  findDataByCode(props.code).then(res => {
    console.log(res);
    dnHisData.value = res.dnData;
    dnPriMin.value = res.dnPriMin;
    dnPriMax.value = res.dnPriMax;
    createDnLine();
  })
}

function createDnLine() {
  setTimeout(() => {
    if (dnChart.value) {
      echarts.dispose(dnChart.value);
    }
    dnChart.value = echarts.init(document.getElementById('mainld'));
    window.addEventListener("resize", () => {
      dnChart.value.resize();
    })
    let option = {
      dataset: {
        dimensions: ['di', 'cp', 'hp', 'lp', 'ap', 'h', 'vol'],
        source: dnHisData.value
      },
      legend: {
        type: 'plain',
        data: ['cp', 'hp', 'lp', 'ap', 'vol', 'h'],
        selected: {
          'cp': true,
          'hp': true,
          'lp': true,
          'ap': true,
          'vol': false,
          'h': true,
        },
        left: 'right',
        top: 'middle',
        orient: 'vertical',
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
        {type: 'value', position: 'right', scale: true, min: 0, alignTicks: true},
        {type: 'value', position: 'right', scale: true, min: 0, alignTicks: true},
      ],
      series: [
        {
          type: 'line',
          name: 'cp',
          encode: {y: 'cp'},
          yAxisIndex: 0,
          lineStyle: {
            color: 'red'
          },
          itemStyle: {
            color: 'red'
          },
          label: {
            show: true,
          },
          emphasis: {
            disabled: true,
          }
        },
        {
          type: 'line',
          name: 'hp',
          encode: {y: 'hp'},
          yAxisIndex: 1,
          areaStyle: {
            color: "rgba(151, 187, 194, 1)",
            opacity: 0.3,
          },
          lineStyle: {
            color: 'green',
            opacity: 0.5,
          },
          itemStyle: {
            color: 'green',
            opacity: 0.5,
          },
          emphasis: {
            focus: 'self',
            label: {
              show: true,
            }
          }
        },
        {
          type: 'line',
          name: 'lp',
          encode: {y: 'lp'},
          yAxisIndex: 2,
          areaStyle: {
            color: "rgba(191, 192, 193, 1)",
            opacity: 0.3,
          },
          lineStyle: {
            color: 'yellow',
            opacity: 0.5,
          },
          itemStyle: {
            color: 'yellow',
            opacity: 0.5,
          },
          emphasis: {
            label: {
              show: true,
              position: 'bottom',
            },
            focus: 'self',
          }
        },
        {
          type: 'line',
          name: 'ap',
          encode: {y: 'ap'},
          yAxisIndex: 3,
          emphasis: {
            focus: 'self',
            label: {
              show: true,
            }
          },
          lineStyle: {
            color: 'blue',
            opacity: 0.5,
          },
          itemStyle: {
            color: 'blue',
            opacity: 0.5,
          },
        },
        {
          name: 'vol',
          type: 'bar',
          encode: {y: 'vol'},
          yAxisIndex: 4,
          label: {
            show: true,
            position: 'bottom',
            rotate: -45,
            offset: [20, 0],
          },
          emphasis: {
            focus: 'self',
            disabled: false,
          },
          z: 2,
        },
        {
          name: 'h',
          type: 'bar',
          encode: {y: 'h'},
          yAxisIndex: 5,
          label: {
            show: true,
            position: 'bottom',
          },
          emphasis: {
            focus: 'self',
            disabled: false,
          },
          itemStyle: {
            color: 'lightblue'
          },
          z: 2,
        }
      ]
    };
    dnChart.value.setOption(option);
  }, 0)
}

</script>

<template>
  <div class="table-container mt-20">
    <div id="mainld" style="width: 100%;height:400px;"></div>
  </div>
</template>

<style scoped>

</style>