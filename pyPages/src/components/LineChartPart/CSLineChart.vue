<script setup>
import echarts from "@/echarts";
import {inject, ref, reactive, shallowRef, watch, computed} from 'vue';
import {isEmpty, tradeDateDecorate, amountFix} from "@/api/util";
import {useStore} from 'vuex'

const store = useStore();
const ldata = inject('ldata', null);
const isDark = computed(() => store.state.isDark);
const chart = reactive({});
const lhdata = reactive({});

const start = ref('');
const textColor = ref('');
updateTextColor();

watch(isDark, () => {
  updateTextColor();
  createLineChart();
})
watch(ldata, () => {
  createLineChart();
})

function updateTextColor() {
  textColor.value = isDark.value ? '#e4e7ed' : '#303133';
}

function createLineChart() {
  if (isEmpty(ldata) || isEmpty(ldata.value)) {
    console.log('no data')
    return;
  }
  lhdata.value = JSON.stringify(JSON.toString(ldata.value));
  let tmpArr = ldata.value.map(obj => {
    return {
      ...obj,
      tradeDate: tradeDateDecorate(obj.tradeDate),
      totalAmount: amountFix(obj.totalAmount),
      zeroAmount: amountFix(obj.zeroAmount),
      threeAmount: amountFix(obj.threeAmount),
      sixAmount: amountFix(obj.sixAmount),
      tu: obj.c00u + obj.c30u + obj.c60u
    };
  });
  start.value = tmpArr[29].tradeDate;
  lhdata.value = tmpArr.reverse();
  createLine();
}

function createLine() {
  setTimeout(() => {
    if (chart.value) {
      echarts.dispose(chart.value);
    }
    chart.value = shallowRef(echarts.init(document.getElementById('mainle')));
    window.addEventListener("resize", () => {
      chart.value.resize();
    })
    let option = {
      dataset: {
        dimensions: ['tradeDate', 'c00u', 'c30u', 'c60u', 'totalAmount', 'zeroAmount', 'threeAmount', 'sixAmount', 'tu'],
        source: lhdata.value
      },
      legend: {
        type: 'plain',
        data: ['tu', '0u', '3u', '6u', 'ta', '0a', '3a', '6a'],
        selected: {
          'tu': true,
          '0u': false,
          '3u': false,
          '6u': false,
          'ta': true,
          '0a': true,
          '3a': true,
          '6a': true,
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
        startValue: start.value,
      }],
      xAxis: {
        type: 'category',
        axisLabel: {
          interval: 0, // 强制显示所有标签
          // formatter: function (value){return value.split('').join('\n')},
        },
      },
      yAxis: [
        {
          type: 'value',
          position: 'left',
          scale: true,
          show: true,
          splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          },
        },
        {
          type: 'value',
          position: 'left',
          scale: true,
          show: false,
          splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          },
        },
        {
          type: 'value',
          position: 'left',
          scale: true,
          show: false,
          splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          },
        },
        {
          type: 'value',
          position: 'right',
          scale: true,
          show: false,
          min:3000,
          max:15000,
          splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value',
          position: 'right',
          scale: true,
          show: false,
          min:1000,
          max: 7000,
          splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value',
          position: 'right',
          scale: true,
          show: false,
          min:1000,
          max: 7000,
          splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value',
          position: 'right',
          show: false,
          scale: true,
          min:1000,
          max: 7000,
          splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          },
        },
        {
          type: 'value',
          position: 'left',
          show: false,
          scale: true,
          splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          },
        },
      ],
      series: [
        {
          type: 'line',
          name: '0u',
          encode: {y: 'c00u'},
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
          name: '3u',
          encode: {y: 'c30u'},
          label: {
            show: false,
          },
          yAxisIndex: 1,
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
          name: '6u',
          encode: {y: 'c60u'},
          label: {
            show: false,
          },
          yAxisIndex: 2,
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
          name: 'ta',
          encode: {y: 'totalAmount'},
          label: {
            show: true,
            color: '#ff00ff',
            position: 'bottom',
            fontWeight: '600',
            // rotate: -90,
            // 确保标签在纵向上正确对齐
            // verticalAlign: 'middle',
            // align: 'left',
            formatter: function (value) {
              return value.data[value.dimensionNames[value.seriesIndex + 1]].toString().replace('.', '·').split('').join('\n')
            },
          },
          yAxisIndex: 3,
          emphasis: {
            focus: 'self',
            label: {
              color: '#ff00ff',
              show: true,
              opacity: 1,
            },
          }
        },
        {
          type: 'bar',
          name: '0a',
          encode: {y: 'zeroAmount'},
          label: {
            show: false,
          },
          itemStyle: {
            opacity: 0.3,
          },
          yAxisIndex: 4,
          emphasis: {
            focus: 'self',
            label: {
              color: textColor.value,
              show: true,
              opacity: 1,
              rotate: 45,
              offset: [10, 0],
            },
          }
        },
        {
          type: 'bar',
          name: '3a',
          encode: {y: 'threeAmount'},
          label: {
            show: false,
          },
          itemStyle: {
            opacity: 0.3,
          },
          yAxisIndex: 5,
          emphasis: {
            focus: 'self',
            label: {
              color: textColor.value,
              show: true,
              opacity: 1,
              rotate: 45,
              offset: [10, 0],
            },
          }
        },
        {
          type: 'bar',
          name: '6a',
          encode: {y: 'sixAmount'},
          label: {
            show: false,
          },
          itemStyle: {
            opacity: 0.3,
          },
          yAxisIndex: 6,
          emphasis: {
            focus: 'self',
            label: {
              color: textColor.value,
              show: true,
              opacity: 1,
              rotate: 45,
              offset: [10, 0],
            },
          }
        },
        {
          type: 'line',
          name: 'tu',
          encode: {y: 'tu'},
          yAxisIndex: 7,
          label: {
            color: textColor.value,
            show: true,
            rotate: 20,
            offset: [10, 0],
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
  }, 0)
}


</script>

<template>
  <div id="mainle" class="line-chart"></div>
</template>

<style scoped>

</style>