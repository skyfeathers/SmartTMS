<template>
    <div class="module">
        <div class="module-header">
            <div class="module-header-icon">
                <img src="/@/assets/images/home/statistics-icon.png" alt="" />
            </div>
            <span class="module-header-title">数字概览</span>
        </div>
        <div class="statistics-list">
            
            <div class="statistics-list-item driver-item" @click="handleClick('/driver/list')">
                <img class="logo" src="/@/assets/images/home/statistics-driver-icon.png" alt="" />
                <span class="number">{{statistics.driverNum}}</span>
                <span class="title">司机</span>
            </div>
            <div class="statistics-list-item vehicle-item" @click="handleClick('/vehicle/vehicle-list')">
                <img class="logo" src="/@/assets/images/home/statistics-vehicle-icon.png" alt="" />
                <span class="number">{{statistics.vehicleNum}}</span>
                <span class="title">车辆</span>
            </div>
            <div class="statistics-list-item order-item" @click="handleClick('/order/list')">
                <img class="logo" src="/@/assets/images/home/statistics-order-icon.png" alt="" />
                <span class="number">{{statistics.orderNum}}</span>
                <span class="title">订单</span>
            </div>
            <div class="statistics-list-item waybill-item" @click="handleClick('/waybill/waybill-list')">
                <img class="logo" src="/@/assets/images/home/statistics-waybill-icon.png" alt="" />
                <span class="number">{{statistics.waybillNum}}</span>
                <span class="title">运单</span>
            </div>
        </div>
    </div>
</template>
<script setup>
import { homeApi } from '/@/api/system/home/home-api';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

let statistics = ref({
    driverNum: 0,
    vehicleNum: 0,
    orderNum: 0,
    waybillNum: 0,
});

onMounted(() => {
    initStatistics();
});

async function initStatistics() {
    try {
        const res = await homeApi.homeNumStatistics();
        statistics.value = res.data || {};
    } catch (error) {
        console.log(error);
    }
}   

function handleClick(url) {
   router.push(url);
}



</script>

<style lang="less" scoped>
.module {
  background-color: #fff;
  border-radius: 5px;
  padding: 15px 20px;
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
}
.statistics-list {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    column-gap: 10px;
    &-item {
        flex: 1;
        display: flex;
        flex-direction: column;
        position: relative;
        padding: 20px 20px;
        background-color: #4382ff;
        border-radius: 10px;
        cursor: pointer;
        &.driver-item {
            background: linear-gradient(135deg, #165DFF 0%, #6997f1 100%);
        }
        &.vehicle-item {
            background: linear-gradient(135deg, #00B42A 0%, #75f192 100%);
            .logo {
                width: 40px;
                height: 60px;
            }
        }
        &.order-item {
            background: linear-gradient(135deg, #FF7D00 0%, #f6bd86 100%);
            .logo {
                width: 50px;
                height: 50px;
            }
        }
        &.waybill-item {
            background: linear-gradient(135deg, #F53F3F 0%, #f67373 100%);
        }

        .logo {
            width: 40px;
            height: 40px;
            position: absolute;
            top: 50%;
            right: 20px;
            transform: translateY(-50%);
            z-index: 1;
        }
        .number {
            position: relative;
            z-index: 2;
            font-size: 24px;
            color: #fff;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .title {
            position: relative;
            z-index: 2;
            font-size: 12px;
            color: #fff;
        }

    }
}
</style>