<script setup>
import {inject, reactive, shallowRef, watch, computed, ref} from 'vue'
import {findLHPByCode} from '@/api/backend'
import {isEmpty, minArr, tradeDateDecorate} from "@/api/util";
import echarts from "@/echarts";
import {useStore} from 'vuex'


const store = useStore();
const isDark = computed(() => store.state.isDark);
const textColor = ref('');
updateTextColor();

const lineCode = inject('lineCode', null)
const chart = reactive({});

watch(lineCode, () => {
  createLineChart();
})

watch(isDark, () => {
  updateTextColor();
  createLineChart();
})

function updateTextColor() {
  textColor.value = isDark.value ? '#e4e7ed' : '#303133';
}


function createLineChart() {
  if (isEmpty(lineCode) || isEmpty(lineCode.value)) {
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
      let start = '';
      if (tmpArr.length < 30) {
        start = tmpArr[0].tradeDate;
      } else {
        start = tmpArr[29].tradeDate;
      }
      tmpArr = tmpArr.reverse();
      let arrProps = minArr(tmpArr, ['tsCode', 'tradeDate'])
      arrProps.start = start;
      createLine(tmpArr, arrProps);
    }
  })
}


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
        dimensions: ['tradeDate', 'lastFivePri', 'lastTenPri', 'lastTwentyPri', 'lastThirtyPri', 'lastFiftyPri', 'lastHundredPri'],
        source: res
      },
      legend: {
        type: 'plain',
        data: ['p5', 'p10', 'p20', 'p30', 'p50', 'p100'],
        selected: {
          'p5': true,
          'p10': false,
          'p20': false,
          'p30': true,
          'p50': false,
          'p100': true,
        },
        right: '10',
        bottom: '20',
        orient: 'vertical',
        textStyle: {
          color: textColor.value,
        },
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
          show: true,
          min: arrProps.min,
          max: arrProps.max,
          splitLine: {
            lineStyle: {
              opacity: 0.9,
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
          name: 'p5',
          encode: {y: 'lastFivePri'},
          yAxisIndex: 0,
          label: {
            show: false,
          },
          emphasis: {
            focus: 'self',
            label: {
              color: textColor.value,
              show: true,
              opacity: 1,
            },
          }
        },
        {
          type: 'line',
          name: 'p10',
          encode: {y: 'lastTenPri'},
          yAxisIndex: 1,
          label: {
            show: false,
          },
          emphasis: {
            focus: 'self',
            label: {
              color: textColor.value,
              show: true,
              opacity: 1,
            },
          }
        },
        {
          type: 'line',
          name: 'p20',
          encode: {y: 'lastTwentyPri'},
          yAxisIndex: 2,
          label: {
            show: false,
          },
          emphasis: {
            focus: 'self',
            label: {
              color: textColor.value,
              show: true,
              opacity: 1,
            },
          }
        },
        {
          type: 'line',
          name: 'p30',
          encode: {y: 'lastThirtyPri'},
          yAxisIndex: 3,
          label: {
            show: false,
          },
          emphasis: {
            focus: 'self',
            label: {
              color: textColor.value,
              show: true,
              opacity: 1,
            },
          }
        },
        {
          type: 'line',
          name: 'p50',
          encode: {y: 'lastFiftyPri'},
          yAxisIndex: 4,
          label: {
            show: false,
          },
          emphasis: {
            focus: 'self',
            label: {
              color: textColor.value,
              show: true,
              opacity: 1,
            },
          }
        },
        {
          type: 'line',
          name: 'p100',
          encode: {y: 'lastHundredPri'},
          yAxisIndex: 5,
          label: {
            show: false,
          },
          emphasis: {
            focus: 'self',
            label: {
              color: textColor.value,
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