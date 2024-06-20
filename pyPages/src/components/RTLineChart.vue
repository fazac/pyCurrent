<script setup>
import echarts from '@/echarts';
import {reactive, watch, shallowRef, ref} from 'vue';

const props = defineProps(['code'])

const rtHisData = reactive([{}]);
const rtChart = reactive({});

const handStep = ref(0);


watch(() => props.code, async (newCode, oldCode) => {
  if (newCode !== undefined && newCode !== null && newCode !== oldCode) {
    prepareRTHisData();
  }
});

function prepareRTHisData() {
  const source = new EventSource("http://localhost:19093/sse/createSSEConnect?clientId=" + props.code);
  source.onmessage = function (event) {
    if (event.lastEventId !== 'sse_client_id') {
      rtHisData.value = JSON.parse(event.data);
      let handFinal = rtHisData.value[rtHisData.value.length - 1].h;
      handStep.value = Math.ceil(handFinal / 4);
      createRTLine();
    }
  }
}

function createRTLine() {
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
          'cp': false,
          'vs': false,
          'h': true,
          'rt': false,
          'bar': false,
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
        {type: 'value', position: 'left', scale: true,},
        {type: 'value', position: 'left', scale: true, show: false},
        {type: 'value', position: 'right', scale: true, alignTicks: true, show: false},
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
          },
          itemStyle: {
            color: 'yellow',
          },
          symbol: "circle",
          emphasis: {
            disabled: false,
          },

        },
        {
          type: 'line',
          name: 'bar',
          encode: {y: 'bar'},
          yAxisIndex: 3,
          symbol: "none",
          emphasis: {
            focus: 'self',
            label: {
              show: false,
            }
          },
          areaStyle: {
            color: "rgba(191, 192, 193, 1)",
            opacity: 0.3,
          },
          lineStyle: {
            color: 'blue',
            opacity: 0.3,
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

</script>

<template>
  <div class="table-container mt-20">
    <div id="mainlc" style="width: 100%;height:400px;"></div>
  </div>
</template>

<style scoped>

</style>