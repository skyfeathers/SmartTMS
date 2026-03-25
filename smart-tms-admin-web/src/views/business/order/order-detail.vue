<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-16 10:05:46
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-02
-->
<template>
  <a-card size="small">
    <a-page-header>
      <template #title>
        {{ `订单单号：${orderDetail.orderNo}` }}<SmartCopyIcon v-if="orderDetail.orderNo" :value="orderDetail.orderNo" />
      </template>
      <template #extra v-if="!cancelFlag">
        <a-button type="primary" @click="showScheduleModal" v-privilege="'order:scheduleDriver'" size="small">分配司机</a-button>
        <a-button type="primary" @click="operateOrder" v-privilege="'order:edit'" size="small">编辑订单</a-button>
        <a-button type="primary" danger  @click="confirmCancel(orderDetail.orderId, getDetail)" v-privilege="'order:cancel'" size="small">取消订单</a-button>
      </template>
      <div class="content">
        <div class="main">
          <a-descriptions :column="4" size="small">
            <a-descriptions-item label="货主简称">{{ orderDetail.shortName }}<SmartCopyIcon v-if="orderDetail.shortName" :value="orderDetail.shortName" /></a-descriptions-item>
            <a-descriptions-item label="本单调度员">{{ orderDetail.scheduleName }}</a-descriptions-item>
            <a-descriptions-item label="销售">{{ orderDetail.managerName }}</a-descriptions-item>
            <a-descriptions-item label="创建人">{{ orderDetail.createUserName }}</a-descriptions-item>

            <a-descriptions-item label="所属区域">{{ orderDetail.areaDesc }}</a-descriptions-item>
            <a-descriptions-item label="运输类型">
              {{ $smartEnumPlugin.getDescByValue('TRANSPORTATION_TYPE_ENUM', orderDetail.businessTypeCode) }}
            </a-descriptions-item>
            <a-descriptions-item label="业务类型" v-if="containerFlag">{{ orderDetail.containerBusinessTypeName }}</a-descriptions-item>
             <a-descriptions-item label="是否分段">
              {{ orderDetail.splitTransportFlag ? '是' : '否' }}
            </a-descriptions-item>
            
            <a-descriptions-item label="柜型" v-if="containerFlag">{{ orderDetail.cabinetName }}</a-descriptions-item>
            <a-descriptions-item label="运输距离(公里)">{{ orderDetail.distance }}</a-descriptions-item>
            <a-descriptions-item label="税点">{{orderDetail.taxPoint}}</a-descriptions-item>
            <a-descriptions-item />
              <a-descriptions-item label="单趟应收运价(元/车)">
                <span style="color: red;">{{ orderDetail.singleTripReceiveAmount }}</span>
              </a-descriptions-item>
              <a-descriptions-item label="单趟应付运价(元/车)">
                <span style="color: red;">{{ orderDetail.singleTripFreightAmount }}</span>
              </a-descriptions-item>
            
            <a-descriptions-item label="客户联系人">{{ orderDetail.shipperContact }}</a-descriptions-item>
            <a-descriptions-item label="客户订单号">{{ orderDetail.shipperOrderNumber }}</a-descriptions-item>
            <a-descriptions-item label="装货时间">{{ orderDetail.loadTime }}</a-descriptions-item>
            <a-descriptions-item label="卸货时间">{{ orderDetail.unloadTime }}</a-descriptions-item>
            <a-descriptions-item label="最迟提箱时间">{{ orderDetail.latestPackingTime }}</a-descriptions-item>
            
            

            <a-descriptions-item label="创建时间">{{ orderDetail.createTime }}</a-descriptions-item>
            <a-descriptions-item label="备注">{{ orderDetail.remark }}</a-descriptions-item>
          </a-descriptions>
        </div>
      </div>
    </a-page-header>
  </a-card>
  <a-card class="smart-margin-top10" size="small">
    <a-tabs>
      <a-tab-pane key="basic" tab="货物信息">
        <OrderDetailBasic :detail="orderDetail"/>
      </a-tab-pane>
      
      <a-tab-pane key="contact" tab="运单信息">
        <OrderWaybillList :orderId="orderDetail.orderId"/>
      </a-tab-pane>
      <a-tab-pane key="contract" tab="相关合同">
        <ContractList :signerType="CONTRACT_SIGNER_TYPE_ENUM.FLEET.value" :signerBelongId="orderDetail.orderId" :orderId="orderDetail.orderId"/>
      </a-tab-pane>
      <a-tab-pane key="operateLog" tab="操作记录">
        <DataTracerList
            :business-id="orderDetail.orderId"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.ORDER.value"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>
  <ScheduleModal ref="scheduleRef"/>
</template>
<script setup>
import { ref, onMounted, computed } from 'vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { orderApi } from '/@/api/business/order/order-api';
import { useRoute, useRouter } from 'vue-router';
import { ORDER_STATUS_ENUM } from '/@/constants/business/order-const';

import OrderDetailBasic from './components/order-detail-basic.vue';
import ContractList from '/@/views/business/contract/contract-list.vue';
import ScheduleModal from './components/schedule-modal.vue';
import OrderWaybillList from './components/order-waybill-list.vue';
import DataTracerList from '/@/components/data-tracer/index.vue';

import { ORDER_TYPE_ENUM } from '/@/constants/business/order-const';
import { CONTRACT_SIGNER_TYPE_ENUM } from '/@/constants/business/contract-const';
import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import { orderCancel } from './hooks/order-cancel';
import { TRANSPORTATION_TYPE_ENUM } from '/@/constants/business/transport-route-const';

// 运输类型为集装箱类型
const containerFlag = computed(() => {
  return orderDetail.value.businessTypeCode == TRANSPORTATION_TYPE_ENUM.CONTAINER_TRANSPORT.value;
});

// 订单类型是否为网络货运
let networkFlag = computed(() => {
  return orderDetail.value.orderType == ORDER_TYPE_ENUM.NETWORK_FREIGHT.value;
});

const cancelFlag = computed(() => {
  return orderDetail.value.status == ORDER_STATUS_ENUM.CANCEL.value;
});

const route = useRoute();
const router = useRouter();

// -------------- 获取订单详情 --------------
let orderId = route.query.orderId;
let orderDetail = ref({
  itemList: [],
  pathList: [],
  mailAddress: null,
});

async function getDetail () {
  if (!orderId) {
    return;
  }
  try {
    useSpinStore().show();
    const { data } = await orderApi.getDetail(orderId);
    orderDetail.value = data;
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

let { confirmCancel } = orderCancel();

function operateOrder () {
  router.push({
    path: '/order/operate',
    query: {
      orderId,
    },
  });
}

const scheduleRef = ref();

function showScheduleModal () {
  scheduleRef.value.showModal(orderId, orderDetail.orderType);
}


onMounted(() => {
  getDetail();
});
</script>
<style lang="less" scoped>
.ant-page-header {
  padding: 0;
}

.content-card {
  ::v-deep(.ant-card-body) {
    padding-top: 0px;
  }
}
</style>
