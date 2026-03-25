<template>
    <div class="module">
        <div class="module-header">
            <div class="module-header-icon">
                <img src="/@/assets/images/home/application-icon.png" alt="" />
            </div>
            <span class="module-header-title">我的应用</span>
        </div>
        <div class="function-entry-list">
            <div class="function-entry-list-item" @click="toLink('/shipper/list')">
                <div class="function-entry-list-item-logo shipper-logo">
                    <img class="logo" src="/@/assets/images/home/shipper-icon.png" alt="" />
                </div>
                <span>货主</span>
            </div>
            <div class="function-entry-list-item" @click="toLink('/driver/list')">
                <div class="function-entry-list-item-logo dirver-logo">
                    <img class="logo" src="/@/assets/images/home/driver-icon.png" alt="" />
                </div>
                <span>司机</span>
            </div>
            <div class="function-entry-list-item" @click="toLink('/vehicle/vehicle-list')">
                <div class="function-entry-list-item-logo vehicle-logo">
                    <img class="logo" src="/@/assets/images/home/vehicle-icon.png" alt="" />
                </div>
                <span>车辆</span>
            </div>
            <div class="function-entry-list-item" @click="toLink('/bracket/list')">
                <div class="function-entry-list-item-logo bracket-logo">
                    <img class="logo" src="/@/assets/images/home/bracket-icon.png" alt="" />
                </div>
                <span>挂车</span>
            </div>
            <div class="function-entry-list-item" @click="toLink('/order/list')">
                <div class="function-entry-list-item-logo order-logo">
                    <img class="logo" src="/@/assets/images/home/order-icon.png" alt="" />
                </div>
                <span>订单</span>
            </div>
            <div class="function-entry-list-item" @click="toLink('/waybill/waybill-list')">
                <div class="function-entry-list-item-logo waybill-logo">
                    <img class="logo" src="/@/assets/images/home/waybill-icon.png" alt="" />
                </div>
                <span>运单</span>
            </div>
            <div class="function-entry-list-item" @click="toLink('/card/oil')">
                <div class="function-entry-list-item-logo fuel-card-logo">
                    <img class="logo" src="/@/assets/images/home/fuel-card-icon.png" alt="" />
                </div>
                <span>油卡</span>
            </div>
            <div class="function-entry-list-item" @click="toLink('/business/oa/flow')">
                <div class="function-entry-list-item-logo audit-logo">
                    <img class="logo" src="/@/assets/images/home/audit-icon.png" alt="" />
                </div>
                <span>审批</span>
            </div>
        </div>
    </div>
</template>
<script setup>
import {useRouter} from 'vue-router';

const router = useRouter();

function toLink(path) {
    router.push(path);
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
        padding: 5px;
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
.function-entry-list {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    &-item {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        font-size: 12px;
        color: #333;
        font-weight: bold;
        letter-spacing: 5px;
        cursor: pointer;
        &-logo {
            width: 45px;
            height: 45px;
            border-radius: 50%;
            margin-bottom: 10px;
            background-color: #3886fb;
            padding: 12px;
            display: flex;
            flex-direction: row;
            justify-content: center;
            align-items: center;
            .logo {
                width: 100%;
                height: 100%;
            }
            &.shipper-logo {
                background-color: #3886fb;
            }
            &.dirver-logo {
                background-color: #ff9800;
            }
            &.vehicle-logo {
                background-color: #00cbd7;
            }
            &.bracket-logo {
                background-color: #ff6154;
            }
            &.order-logo {
                background-color: #a26cff;
            }
            &.waybill-logo {
                background-color: #00c35d;
            }
            &.fuel-card-logo {
                background-color: #ffc107;
            }
            &.audit-logo {
                background-color: #9c27b0;
            }
        }
        
    }
}
</style>