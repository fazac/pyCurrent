<script setup>
import echarts from '@/echarts';
import {reactive, ref, watch, shallowRef} from 'vue';
import {findDataLineByCode} from '@/api/backend.js'


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
  findDataLineByCode(props.code).then(res => {
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
    dnChart.value = shallowRef(echarts.init(document.getElementById('mainld')));
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
            offset: [0, 20],
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
  <div id="mainld" class="table-container"></div>
</template>

<style scoped>

</style>