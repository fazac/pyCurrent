<script setup>
import {inject, reactive, shallowRef, watch} from 'vue'
import {findLHPByCode} from '@/api/backend'
import {isEmpty, minArr, tradeDateDecorate} from "@/api/util";
import echarts from "@/echarts";

const lineCode = inject('lineCode', null)
const chart = reactive({});

watch(lineCode, () => {
  if (isEmpty(lineCode)) {
    return
  }
  findLHPByCode(lineCode.value).then(res => {
    if (!isEmpty(res)) {
      let tmpArr = res.map(obj => {
        return {
          ...obj,
          tradeDate: tradeDateDecorate(obj.tradeDate),
        };
      });
      tmpArr = tmpArr.reverse();
      let arrProps = minArr(tmpArr, ['tsCode', 'tradeDate'])
      createLine(tmpArr, arrProps);
    }
  })
})

function createLine(res, arrProps) {
  setTimeout(() => {
    if (chart.value) {
      echarts.dispose(chart.value);
    }
    chart.value = shallowRef(echarts.init(document.getElementById('mainlf')));
    window.addEventListener("resize", () => {
      chart.value.resize();
    })
    let option = {
      dataset: {
        dimensions: ['tradeDate', 'currentPri', 'lastFivePri', 'lastTenPri', 'lastTwentyPri', 'lastThirtyPri', 'lastFiftyPri', 'lastHundredPri'],
        source: res
      },
      legend: {
        type: 'plain',
        data: ['cp', 'p5', 'p10', 'p20', 'p30', 'p50', 'p100'],
        selected: {
          'cp': true,
          'p5': false,
          'p10': false,
          'p20': true,
          'p30': false,
          'p50': true,
          'p100': true,
        },
        right: '10',
        bottom: '20',
        orient: 'vertical',
      },
      dataZoom: [{
        show: true,
        startValue: arrProps.start
      }],
      xAxis: {
        type: 'category',
      },
      yAxis: [
        {
          type: 'value',
          position: 'left',
          scale: true,
          show: false,
          min: arrProps.min,
          max: arrProps.max,
          splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value',
          position: 'left',
          scale: true,
          show: false,
          min: arrProps.min,
          max: arrProps.max,
          splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value',
          position: 'left',
          scale: true,
          show: false,
          min: arrProps.min,
          max: arrProps.max,
          splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value',
          position: 'left',
          scale: true,
          show: false,
          min: arrProps.min,
          max: arrProps.max,
          splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value',
          position: 'left',
          scale: true,
          show: false,
          min: arrProps.min,
          max: arrProps.max,
          splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value',
          position: 'left',
          scale: true,
          show: false,
          min: arrProps.min,
          max: arrProps.max,
          splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value',
          position: 'left',
          scale: true,
          show: false,
          min: arrProps.min,
          max: arrProps.max,
          splitLine: {
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
          encode: {y: 'currentPri'},
          yAxisIndex: 0,
          label: {
            show: false,
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
          name: 'p5',
          encode: {y: 'lastFivePri'},
          yAxisIndex: 1,
          label: {
            show: false,
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
          name: 'p10',
          encode: {y: 'lastTenPri'},
          yAxisIndex: 2,
          label: {
            show: false,
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
          name: 'p20',
          encode: {y: 'lastTwentyPri'},
          yAxisIndex: 3,
          label: {
            show: false,
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
          name: 'p30',
          encode: {y: 'lastThirtyPri'},
          yAxisIndex: 4,
          label: {
            show: false,
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
          name: 'p50',
          encode: {y: 'lastFiftyPri'},
          yAxisIndex: 5,
          label: {
            show: false,
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
          name: 'p100',
          encode: {y: 'lastHundredPri'},
          yAxisIndex: 6,
          label: {
            show: false,
          },
          emphasis: {
            focus: 'self',
            label: {
              show: true,
              opacity: 1,
            },
          }
        },

      ]
    };
    chart.value.setOption(option);
  })
}

</script>

<template>
  <div id="mainlf" class="line-chart"></div>
</template>

<style scoped>

</style>