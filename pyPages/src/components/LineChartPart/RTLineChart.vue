<script setup>
import echarts from '@/echarts';
import {inject, onUnmounted, reactive, shallowRef, watch} from 'vue';
import {isEmpty, baseUrl, verticalValueChart} from "@/api/util";

const lineCode = inject('lineCode', null);

const rtHisData = reactive([{}]);
const rtChart = reactive({});

const sourceList = [];

watch(lineCode, async () => {
  if (!isEmpty(lineCode) && !isEmpty(lineCode.value)) {
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
  const source = new EventSource(baseUrl + "/sse/createSSEConnect?clientId=" + lineCode.value + "&type=2");
  source.onmessage = function (event) {
    if (event.lastEventId !== 'sse_client_id') {
      rtHisData.value = JSON.parse(event.data);
      let handsArr = rtHisData.value.map((item) => {
        return item.h;
      })
      let handFinal = handsArr[handsArr.length - 1];
      let handStep = Math.ceil(handFinal / 4);
      let xArr = calXIndex(handsArr, handStep);
      let xArrFinal = calXPoint(handsArr, handStep);
      createRTLine(xArr, xArrFinal, handStep * 4);
    }
  }
  source.onerror = function () {
    source.close();
  };
  sourceList.push(source);
}

function calXIndex(handsArr, handStep) {
  let xArr = [];
  let xArrFinal = [];
  handsArr.reduce(function (pre, cur, index) {
    if (cur !== pre && (cur === handStep || cur === handStep * 2 || cur === handStep * 3)) {
      xArr.push(index);
    } else if (pre < handStep && cur > handStep
        || pre < handStep * 2 && cur > handStep * 2
        || pre < handStep * 3 && cur > handStep * 3) {
      xArr.push(index - 1)
    }
    return cur;
  }, null);
  xArr.push(handsArr.length - 1);
  xArr = xArr.filter(function (item, index, arr) {
    return arr.indexOf(item, 0) === index && item > 0;
  })
  xArr.sort((a, b) => a - b);
  xArr.reduce(function (pre, cur, index) {
    xArrFinal.push({
      coord: [cur, handStep * 4],
      value: cur + '\n' + handsArr[cur],
    });
    return null;
  }, null);
  return xArrFinal;
}

function calXPoint(handsArr, handStep) {
  let xArr = [];
  let xArrFinal = [];
  let colors = ['#5470c6', '#91cc75', '#fac858', '#ee6666'];
  handsArr.reduce(function (pre, cur, index) {
    if (cur !== pre && (cur === handStep || cur === handStep * 2 || cur === handStep * 3)) {
      xArr.push(index);
    } else if (pre < handStep && cur > handStep
        || pre < handStep * 2 && cur > handStep * 2
        || pre < handStep * 3 && cur > handStep * 3) {
      xArr.push((index - 1))
    }
    return cur;
  }, null);
  xArr.push(handsArr.length - 1);
  xArr = xArr.filter(function (item, index, arr) {
    return arr.indexOf(item, 0) === index && item > 0;
  })
  xArr.sort((a, b) => a - b);
  xArr.reduce(function (pre, cur, index) {
    if (pre === null) {
      xArrFinal.push([{
        xAxis: 0, itemStyle: {
          color: colors[index], opacity: 0.2
        },
      }, {xAxis: cur}]);
    } else {
      xArrFinal.push([{
        xAxis: pre, itemStyle: {
          color: colors[index], opacity: 0.2
        },
      }, {xAxis: cur}]);
    }
    return cur;
  }, null);
  return xArrFinal;
}

function createRTLine(xArr, xArrFinal, maxHand) {
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
        right: '10',
        bottom: '10',
        orient: 'vertical',
      },
      xAxis: {
        type: 'category',
        show: false,
        // axisLabel: {
        //   formatter: function (value, index) {
        //     console.log(value)
        //     if (xArr.indexOf(value) >= 0) {
        //       return value;
        //     } else {
        //       return '';
        //     }
        //   }
        // }
      },
      yAxis: [
        {
          type: 'value', position: 'left', scale: true, splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value', position: 'right', scale: true, alignTicks: true, splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value', position: 'left', scale: true, max: maxHand, min: 0, show: false, splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value', position: 'left', scale: true, show: false, splitLine: {
            lineStyle: {
              opacity: 0.1,
            }
          }
        },
        {
          type: 'value', position: 'right', scale: true, show: false, splitLine: {
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
            color: 'red'
          },
          itemStyle: {
            color: 'red'
          },
          symbol: "circle",
          symbolSize: 3,
          emphasis: {
            focus: 'self',
            label: {
              color: 'red',
              show: true,
              opacity: 1,
              position: 'bottom',
              offset: [0, 10],
              formatter: verticalValueChart,
            },
          },
          markLine: {
            precision: 2,
            symbol: 'none',
            data: [{type: 'average'}],
            label: {
              offset: [25, 0],
              color: 'red',
            }
          },
          markPoint: {
            data: [{
              type: "max",
            }, {
              type: "min",
              symbolRotate: 180,
              label: {
                offset: [0, 12]
              }
            }],
            symbol: "pin",
            label: {
              color: '#bbff33',
              opacity: 0.8,
            },
            itemStyle: {
              opacity: 0.4,
            },
          },
          endLabel: {
            show: true,
            color: 'red',
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
          },
          markPoint: {
            data: [{
              type: "max",
            }, {
              type: "min",
              symbolRotate: 180,
              label: {
                offset: [0, 12]
              }
            }],
            symbol: "pin",
            label: {
              color: 'green',
              opacity: 0.8,
            },
            itemStyle: {
              opacity: 0.4,
            },
          },
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
            data: xArrFinal,
          },
          markPoint: {
            symbol: 'diamond',
            data: xArr,
            label: {
              show: true,
              color: '#ff00ff',
              // color: 'yellow',
              opacity: 1,
              fontWeight: 'bolder'
            },
            symbolOffset: [0, '-50%'],
            itemStyle: {
              opacity: 0.1,
            },
          },
          // endLabel: {
          //   show: true,
          //   formatter: '{@h}'
          // }
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
            label: {
              color: 'red',
              show: true,
              opacity: 1,
              position: 'top',
              offset: [10,8],
              rotate:90,
            },
          },
          itemStyle: {
            color: '#ff6666',
            opacity: 0.3,
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
  <div id="mainlc" class="line-chart"></div>
</template>

<style scoped>

</style>