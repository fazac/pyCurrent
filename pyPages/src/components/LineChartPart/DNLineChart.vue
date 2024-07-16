<script setup>
import echarts from '@/echarts';
import {reactive, ref, shallowRef, watch, inject} from 'vue';
import {findDataLineByCode} from '@/api/backend.js'
import {calRatio, isEmpty, nfc} from "@/api/util";


const lineCode = inject('lineCode', null);

const dnHisData = reactive([{}]);
const dnPriMin = ref(0);
const dnPriMax = ref(0);
const start = ref(0);
const dnChart = reactive({});
const pointArr = [];

watch(lineCode, async () => {
  if (!isEmpty(lineCode) && !isEmpty(lineCode.value)) {
    prepareDnHisData();
  }
});

function prepareDnHisData() {
  findDataLineByCode(lineCode.value).then(res => {
    dnHisData.value = res.dnData;
    start.value = res.dnData.length > 30 ? res.dnData.length - 30 : 0;
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
    dnChart.value = shallowRef(echarts.init(document.getElementById('mainld'), {useCoarsePointer: true}));
    window.addEventListener("resize", () => {
      dnChart.value.resize();
    })
    dnChart.value.on('click', function (params) {
      pointArr.push(params.data.cp);
      console.log(pointArr)
      if (pointArr.length > 1) {
        nfc('ratio', "start: " + pointArr[pointArr.length - 2] + " end: " + pointArr[pointArr.length - 1] + " ratio: " + calRatio(pointArr[pointArr.length - 2], pointArr[pointArr.length - 1]));
      }
    });
    let option = {
      dataset: {
        dimensions: ['di', 'cp', 'hp', 'lp', 'ap', 'h', 'vol'],
        source: dnHisData.value
      },
      dataZoom: [{
        show: true,
        startValue: start.value,
      }],
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
        right: '10',
        bottom: '10',
        orient: 'vertical',
      },
      xAxis: {
        type: 'category',
        show: false
      },
      yAxis: [
        {
          type: 'value',
          position: 'left',
          scale: true,
          min: dnPriMin.value,
          max: dnPriMax.value,
          splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value', position: 'left', scale: true, min: dnPriMin.value, max: dnPriMax.value, splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value', position: 'left', scale: true, min: dnPriMin.value, max: dnPriMax.value, splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value', position: 'left', scale: true, min: dnPriMin.value, max: dnPriMax.value, splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value', position: 'right', scale: true, min: 0, splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value', position: 'right', scale: true, min: 0, splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
      ],
      series: [
        {
          type: 'line',
          name: 'cp',
          encode: {y: 'cp'},
          yAxisIndex: 0,
          lineStyle: {
            color: 'red',
          },
          itemStyle: {
            color: 'red'
          },
          label: {
            show: true,
            color: '#ff00ff',
            fontWeight: '600',
          },
          emphasis: {
            disabled: true,
          }
        },
        {
          type: 'line',
          name: 'hp',
          encode: {y: 'hp'},
          label: {
            show: true,
            opacity: 1,
            distance: 13,
            color: '#ff00ff',
            fontWeight: 'bolder',
          },
          yAxisIndex: 1,
          areaStyle: {
            color: "rgba(191, 192, 193, 1)",
            opacity: 0.3,
            origin: 'end',
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
              opacity: 1,
            },
          }
        },
        {
          type: 'line',
          name: 'lp',
          encode: {y: 'lp'},
          yAxisIndex: 2,
          label: {
            show: true,
            opacity: 1,
            distance: 13,
            position: 'bottom',
            color: '#ff00ff',
            fontWeight: 'bolder',
          },
          areaStyle: {
            color: "rgba(191, 192, 193, 1)",
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
            label: {
              show: true,
              position: 'bottom',
              opacity: 1,
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
              opacity: 1,
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
            offset: [20, 20],
            fontWeight: "bolder",
            color: '#ff6666'
          },
          emphasis: {
            focus: 'self',
            disabled: false,
          },
          itemStyle: {
            color: '#ff6666',
            opacity: 0.3,
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
            opacity: 1,
            rotate: -45,
            offset: [20, 20],
            fontWeight: "bolder",
            color: '#ff6666'
          },
          emphasis: {
            focus: 'self',
            disabled: false,
          },
          itemStyle: {
            color: '#ff6666',
            opacity: 0.3,
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
  <div id="mainld" class="line-chart"></div>
</template>

<style scoped>

</style>