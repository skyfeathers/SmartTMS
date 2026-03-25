<template>
  <div class="bar-main" id="clue-call"></div>
</template>
<script setup>
import * as echarts from 'echarts/core';
import { BarChart,LineChart } from 'echarts/charts';
import { CanvasRenderer } from 'echarts/renderers';
import { GridComponent } from 'echarts/components';
/* 引入echarts工具 */
import 'echarts/lib/component/tooltip'
import 'echarts/lib/component/title'
import 'echarts/lib/component/legend'
import { onMounted, watch, reactive, nextTick, ref } from 'vue';
// ----------------------- 以下是字段定义 emits props ---------------------
const props =  defineProps({
  chartsData:{
    type: Object
  }
});
let option = reactive({});
// ----------------------- 以下是生命周期 ---------------------------------
watch(
    () => props.chartsData,
    (value) => {
      init();
    }
);
// ----------------------- 以下是生命周期 ---------------------------------
echarts.use([BarChart,LineChart,GridComponent, CanvasRenderer]);
// ----------------------- 以下是方法 ------------------------------------

async function init() {
  let xAxis = props.chartsData.xAxis;
  let yAxis = props.chartsData.yAxis;
  option = {
    title: {
      text: '客户新增统计'
    },
    tooltip: {
      trigger: 'axis',
    },
    xAxis: {
      type: 'category',
      // data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      data: xAxis
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        // data: [150, 230, 224, 218, 135, 147, 260],
        name: '新增',
        data: yAxis,
        type: 'line',
        label: {
          show: true,
          position: 'top'
        }
      }
    ]
  };
  var chartDom = document.getElementById('clue-call');
  if (chartDom) {
    let myChart = echarts.init(chartDom);
    option && myChart.setOption(option);
  }
}

// ----------------------- 以下是暴露的方法内容 ----------------------------
defineExpose({});
</script>
<style scoped lang="less">
.bar-main {
  height: 360px;
  width: 100%;
}
</style>
