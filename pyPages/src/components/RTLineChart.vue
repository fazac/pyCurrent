<script setup>
import echarts from '@/echarts';
import {reactive, watch, shallowRef, onUnmounted} from 'vue';

const props = defineProps(['code'])

const rtHisData = reactive([{}]);
const rtChart = reactive({});

const sourceList = [];

watch(() => props.code, async (newCode, oldCode) => {
  if (newCode !== undefined && newCode !== null && newCode !== oldCode) {
    prepareRTHisData();
  }
});

function prepareRTHisData() {
  if (sourceList !== undefined && sourceList.length > 0) {
    sourceList.forEach((item) => {
      item.close();
    })
    sourceList.length = 0;
  }
  const source = new EventSource("http://localhost:19093/sse/createSSEConnect?clientId=" + props.code);
  source.onmessage = function (event) {
    if (event.lastEventId !== 'sse_client_id') {
      rtHisData.value = JSON.parse(event.data);
      let handsArr = rtHisData.value.map((item) => {
        return item.h;
      })
      let handFinal = handsArr[handsArr.length - 1];
      let handStep = Math.ceil(handFinal / 4);
      let xArrFinal = calXPoint(handsArr, handStep);
      createRTLine(xArrFinal);
    }
  }
  sourceList.push(source);
}

function calXPoint(handsArr, handStep) {
  let xArr = [];
  let xArrFinal = [];
  let colors = ['#5470c6', '#91cc75', '#fac858', '#ee6666'];
  handsArr.reduce(function (pre, cur, index) {
    if (cur === handStep || cur === handStep * 2 || cur === handStep * 3) {
      xArr.push(index);
    }
    if (pre < handStep && cur > handStep) {
      xArr.push((1 - pre) / (cur - pre) + (index - 1))
    }
    if (pre < handStep * 2 && cur > handStep * 2) {
      xArr.push((1 - pre) / (cur - pre) + (index - 1))
    }
    if (pre < handStep * 3 && cur > handStep * 3) {
      xArr.push((1 - pre) / (cur - pre) + (index - 1))
    }
    return cur;
  }, null);
  xArr.push(handsArr.length - 1);
  xArr.filter(function (item, index, arr) {
    return arr.indexOf(item, 0) === index && item > 0;
  }).sort((a, b) => a - b);
  xArr.reduce(function (pre, cur, index) {
    if (pre === null) {
      xArrFinal.push([{
        xAxis: 0, itemStyle: {
          color: colors[index], opacity: 0.2
        }, name: handStep
      }, {xAxis: cur}]);
    } else {
      xArrFinal.push([{
        xAxis: pre, itemStyle: {
          color: colors[index], opacity: 0.2
        }, name: handStep * (index + 1)
      }, {xAxis: cur}]);
    }
    return cur;
  }, null);
  return xArrFinal;
}

function createRTLine(xArr) {
  setTimeout(() => {
    if (rtChart.value) {
      echarts.dispose(rtChart.value);
    }
    rtChart.value = shallowRef(echarts.init(document.getElementById('mainlc')));
    window.addEventListener("resize", () => {
      rtChart.value.resize();
    })
    let option = {
      dataset: {
        dimensions: ['di', 'cp', 'vs', 'h', 'rt', 'bar'],
        source: rtHisData.value
      },
      legend: {
        type: 'plain',
        data: ['cp', 'vs', 'h', 'rt', 'bar'],
        selected: {
          'cp': true,
          'vs': true,
          'h': true,
          'rt': false,
          'bar': true,
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
        {type: 'value', position: 'left', scale: true,},
        {type: 'value', position: 'right', scale: true, alignTicks: true},
        {type: 'value', position: 'left', scale: true, show: false},
        {type: 'value', position: 'left', scale: true, show: false},
        {type: 'value', position: 'right', scale: true, show: false},
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
          symbol: "circle",
          symbolSize: 3,
          emphasis: {
            disabled: true,
          },
          markPoint: {
            data: [{
              type: "max",
            }, {
              type: "min",
            }],
            symbol: "pin",
            label: {
              color: 'black',
            },
            itemStyle: {
              opacity: 0.4,
            },
          },
          endLabel: {
            show: true,
            formatter: '{@cp}'
          }
        },
        {
          type: 'line',
          name: 'rt',
          encode: {y: 'rt'},
          yAxisIndex: 1,
          lineStyle: {
            color: 'green',
            opacity: 0.5,
          },
          itemStyle: {
            color: 'green',
            opacity: 0.5,
          },
          symbol: "circle",
          symbolSize: 3,
          emphasis: {
            disabled: true,
          }
        },
        {
          type: 'line',
          name: 'h',
          encode: {y: 'h'},
          yAxisIndex: 2,
          lineStyle: {
            color: 'yellow',
            opacity: 0.7,
          },
          itemStyle: {
            color: 'yellow',
          },
          symbol: "none",
          emphasis: {
            disabled: false,
          },
          markArea: {
            data: xArr,
          },
          endLabel: {
            show: true,
            formatter: '{@h}'
          }
        },
        {
          type: 'line',
          name: 'bar',
          encode: {y: 'bar'},
          yAxisIndex: 3,
          symbol: "none",
          emphasis: {
            disabled: true,
            // focus: 'self',
            // label: {
            //   show: false,
            // }
          },
          areaStyle: {
            color: "rgba(191, 192, 193, 1)",
            opacity: 0.2,
          },
          lineStyle: {
            color: 'blue',
            opacity: 0.1,
          },
        },
        {
          name: 'vs',
          type: 'bar',
          encode: {y: 'vs'},
          yAxisIndex: 4,
          label: {
            show: false,
          },
          emphasis: {
            focus: 'self',
            disabled: false,
          },
          itemStyle: {
            color: 'lightblue'
          },
          z: 2,
        },
      ]
    };
    rtChart.value.setOption(option);
  }, 0)
}

onUnmounted(() => {
  if (sourceList !== undefined && sourceList.length > 0) {
    sourceList.forEach((item) => {
      item.close();
    })
    sourceList.length = 0;
  }
})

</script>

<template>
  <div id="mainlc" class="table-container"></div>
</template>

<style scoped>

</style>