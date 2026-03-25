<template>
    <div class="module">
        <div class="module-header">
            <div class="module-header-icon">
                <img src="/@/assets/images/home/count-icon.png" alt="" />
            </div>
            <span class="module-header-title">证件过期分析</span>
        </div>
        <div class="module-content">
            <div class="module-content-chart" id="credentials-count"></div>
        </div>
    </div>
</template>
<script setup>
import * as echarts from 'echarts';
import {onMounted, onUnmounted, ref} from "vue";
import { homeApi } from "/@/api/system/home/home-api";

onMounted(() => {
  init();
});

onUnmounted(() => {
    if (echartsInstance.value) {
        echartsInstance.value.dispose();
    }
});

let echartsInstance = ref(null);

async function init() {
    let res = await homeApi.homeExpireCertSummary();
    
    let option = {

        legend: {
            top: 'bottom',
            left: 'center',
            width: 300,
            formatter: "{a|{name}}",
            textStyle: {
              overflow: 'truncate',
              rich: {
                a: {
                  width: 60,
                  backgroundColor: "rgba(11, 39, 52, 0)"
                },
              },
            },
        },
         tooltip: {
            trigger: 'item',
            formatter: '{b} : {c}'
        },
        series: [
            {
                name: '证件过期分析',
                type: 'pie',
                radius: ['40%', '70%'],
                center: ['50%', '40%'],
                itemStyle: {
                    borderRadius: 8
                },
                label: {
                    show: false,
                },
                data: res.data || [],
            }
        ]
    };
    let chartDom = document.getElementById("credentials-count");
    if (chartDom) {
        echartsInstance.value = echarts.init(chartDom);
        option && echartsInstance.value.setOption(option);
         window.addEventListener('resize', () => {
            if (echartsInstance.value) {
                echartsInstance.value.resize();
            }
        });
    }
}

</script>

<style lang="less" scoped>
.module {
  height: 100%;
  background-color: #fff;
  border-radius: 5px;
  padding: 10px 20px;
  display: flex;
  flex-direction: column;
  &-header {
    margin-bottom: 20px;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    column-gap: 10px;
    &-icon {
        width: 20px;
        height: 20px;
        background-color: #3886fb;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        border-radius: 5px;
        padding: 4px;
        box-sizing: border-box;
        // 蓝色阴影
        box-shadow: 0px 0px 5px 0px rgba(56, 134, 251, 0.5);
        img {
            width: 100%;
            height: 100%;
        }
    }
    &-title {
      font-size: 14px;
      color: #333;
      font-weight: bold;
    }
  }
  &-content {
    width: 100%;
    flex: 1;
    &-chart {
        width: 100%;
        height: 100%;
    }
  }
}

</style>
